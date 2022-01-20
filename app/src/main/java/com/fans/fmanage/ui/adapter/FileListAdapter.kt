package com.fans.fmanage.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.fans.fmanage.R
import com.fans.fmanage.bean.FileInfo
import com.fans.fmanage.bean.FileListInfo
import com.fans.fmanage.databinding.LayoutItemFileListBinding
import com.fans.fmanage.ui.FileListActivity
import com.fans.fmanage.utils.StorageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FileListAdapter() : RecyclerView.Adapter<FileListAdapter.FileListViewHolder>() {
    private val TAG = "FileListAdapter"
    private var mContext: Context? = null
    var mFileListInfo: FileListInfo? = null
    var listener:SubDirectoryListener? =null

    constructor(context: Context, mFileListInfo: FileListInfo) : this() {
        this.mContext = context
        this.mFileListInfo = mFileListInfo

    }


    fun loadData(mFileListInfo: FileListInfo){
        this.mFileListInfo = mFileListInfo
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileListViewHolder {
        val dataBinding = DataBindingUtil.inflate<LayoutItemFileListBinding>(
            LayoutInflater.from(mContext),
            R.layout.layout_item_file_list,
            parent,
            false
        );
        return FileListViewHolder(dataBinding!!.root, dataBinding)

    }

    override fun onBindViewHolder(holder: FileListViewHolder, position: Int) {
        val fileInfo = mFileListInfo?.filesInfo?.get(position)
        holder.binding.model = fileInfo
        holder.binding.adapter = this
        holder.binding.holder = holder
        holder.fileName.isSelected = fileInfo?.isSelected!!
        holder.fileName.text = fileInfo.fileName
        if (fileInfo.isDirectory) holder.fileImg.setImageResource(R.mipmap.directory) else holder.fileImg.setImageResource(
            R.mipmap.file
        )


    }

    private fun clearFileState() {
        mFileListInfo?.filesInfo?.forEach {
            it.isSelected = false
        }
    }

    override fun getItemCount(): Int {
        val count = (mFileListInfo?.filesInfo?.size) ?: 0
        return if (count == 0) 0 else count
    }


    class FileListViewHolder(var itemView: View, var binding: LayoutItemFileListBinding) :
        RecyclerView.ViewHolder(itemView) {
        var fileName: TextView = binding.fileName
        var fileImg: ImageView = binding.fileImg
        var view: View = itemView
    }


    //dataBinding绑定的点击事件
    fun clickItem(fileInfo: FileInfo, holder: FileListViewHolder) {
        if (!fileInfo.isDirectory) {
            clickFile()(fileInfo, holder)
            return
        }
        clickDirectory(fileInfo)

    }


    // 处理点击文件
    private fun clickFile() = { fileInfo: FileInfo, holder: FileListViewHolder ->
        holder.fileName.isSelected = !(holder.fileName.isSelected)
        clearFileState()
        fileInfo.isSelected = !fileInfo.isSelected
        notifyDataSetChanged()
    }


    /**
     * 处理点击文件夹的函数
     */
    val clickDirectory = { fileInfo: FileInfo ->

           listener?.enterSubDirectory(context = mContext!!,fileInfo)

    }


    interface SubDirectoryListener{
        fun enterSubDirectory(context: Context,fileInfo: FileInfo)
    }

}
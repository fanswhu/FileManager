package com.fans.fmanage.viewmodel

import android.app.Activity
import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fans.fmanage.bean.FileInfo
import com.fans.fmanage.bean.FileListInfo
import com.fans.fmanage.ui.FileListActivity
import com.fans.fmanage.ui.adapter.FileListAdapter
import com.fans.fmanage.utils.AppConfig
import com.fans.fmanage.utils.StorageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FileListViewModel(application: Application) : AndroidViewModel(application),
    FileListAdapter.SubDirectoryListener {
    var app: Application = getApplication<Application>()

    val selectLiveData = MutableLiveData<String>()
    val backLiveData = MutableLiveData<Int>()
    val stateLiveData = MutableLiveData<Int>()

    companion object {
        const val SHOW_CONTENT = 1;
        const val SHOW_LOADING = 2;
        const val SHOW_EMPTY = 3;
        const val SHOW_ERROR = 4;
    }

    val mAdapter: FileListAdapter by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        stateLiveData.value = SHOW_LOADING
        val emptyList = FileListInfo(ArrayList<FileInfo>(), 0, null, "")
        GlobalScope.launch(Dispatchers.Main) {
            val fileListInfo = StorageUtils.readSubFilesFromPath(AppConfig.ROOT_PATH, null, 0)
            mAdapter.loadData(fileListInfo)
            if ((mAdapter.mFileListInfo?.filesInfo?.size!! > 0)) {
                stateLiveData.value = SHOW_CONTENT
            } else {
                stateLiveData.value = SHOW_EMPTY
            }
            mAdapter.notifyDataSetChanged()
            mAdapter.listener = this@FileListViewModel
        }
        FileListAdapter(app, emptyList)

    }


    fun onBack(flag: Boolean) {
        if (mAdapter.mFileListInfo?.parentFileList != null && !flag) {
            mAdapter.mFileListInfo = mAdapter.mFileListInfo?.parentFileList
            mAdapter.notifyDataSetChanged()
            backLiveData.value = mAdapter.mFileListInfo?.scrollDiff
            mAdapter.mFileListInfo?.scrollDiff = 0
            stateLiveData.value = SHOW_CONTENT
        } else {
            backLiveData.value = null
        }
    }

    fun confirm() {
        mAdapter.mFileListInfo?.filesInfo?.forEach {
            if (it.isSelected) {
                selectLiveData.value = it.path
                return
            }
        }
        selectLiveData.value = mAdapter.mFileListInfo?.currentDirectory

    }

    fun saveScrollDiff(dy: Int) {
        mAdapter.mFileListInfo!!.scrollDiff += dy
        Log.d(TAG, "saveScrollDiff:${mAdapter.mFileListInfo!!.scrollDiff}")
    }


    override fun enterSubDirectory(context: Context, fileInfo: FileInfo) {
        GlobalScope.launch(Dispatchers.Main) {
            stateLiveData.value = SHOW_LOADING
            val fileListInfo = StorageUtils.readSubFilesFromPath(
                fileInfo.path,
                mAdapter.mFileListInfo,
                0
            )
            mAdapter.mFileListInfo = fileListInfo
            mAdapter.notifyDataSetChanged()
            if ((mAdapter.mFileListInfo?.filesInfo?.size!! > 0)) {
                stateLiveData.value = SHOW_CONTENT
            } else {
                stateLiveData.value = SHOW_EMPTY
            }
        }

    }

}
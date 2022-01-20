package com.fans.fmanage.utils

import android.util.Log
import com.fans.fmanage.bean.FileInfo
import com.fans.fmanage.bean.FileListInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


object StorageUtils {
    private val TAG = "StorageUtils"
    suspend fun readSubFilesFromPath(
        path: String,
        parentListInfo: FileListInfo?,
        scrollDiff: Int
    ): FileListInfo {
        var result: FileListInfo? = null
        withContext(Dispatchers.IO) {
            val parentFile = File(path)
            if (!parentFile.isDirectory) {
                result = FileListInfo(ArrayList<FileInfo>(), scrollDiff, parentListInfo, path)
            }
            val fileList = ArrayList<FileInfo>()

            parentFile.listFiles()?.forEach {
                val info = FileInfo(
                    it.name, it.absolutePath, it.lastModified(),
                    it.totalSpace, it.isDirectory, false
                )
                fileList.add(info)
            }

            result = FileListInfo(fileList, scrollDiff, parentListInfo, path)

        }

        Log.d(TAG,"get ${result?.currentDirectory} files success")
        return result!!
    }

}
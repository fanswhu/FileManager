package com.fans.fmanage.bean

data class FileListInfo(val filesInfo:MutableList<FileInfo>,var scrollDiff:Int,
                        var parentFileList: FileListInfo?,var currentDirectory:String)

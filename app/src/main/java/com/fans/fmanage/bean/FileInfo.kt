package com.fans.fmanage.bean

import android.graphics.drawable.Drawable

data class FileInfo(var fileName:String,var path:String,var createTime:Long,var size:Long
,val isDirectory:Boolean, var isSelected:Boolean)

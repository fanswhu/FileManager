package com.fans.fmanage.viewmodel

import android.app.AppComponentFactory
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.fans.fmanage.core.FilePicker
import com.fans.fmanage.ui.FileListActivity
import com.fans.fmanage.utils.AppConfig

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var app: Application = getApplication<Application>()

    val test="Test"
    val liveData = MutableLiveData<String>()

    fun openBrowser() {
       liveData.value = ""
    }

}
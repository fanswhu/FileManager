package com.fans.fmanage.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.fans.fmanage.ui.FileListActivity
import com.fans.fmanage.utils.AppConfig

class FilePicker private constructor() {

    private var pickParams: PickParams? = null

    companion object {
        const val REQUEST_CODE = 10037
        const val RESULT_CODE = 10037
        const val KEY_DATA = "data"
        fun build(): Builder {
            return Builder()
        }
    }

    class Builder {
        private val pickParams =
            PickParams(multiSupport = false, fileSupport = false, path = AppConfig.ROOT_PATH)

        fun buildMultiSupport(): Builder {
            pickParams.multiSupport = true
            return this
        }

        fun buildFileSupport(): Builder {
            pickParams.fileSupport = true
            return this
        }

        fun buildPath(path: String): Builder {
            pickParams.path = path
            return this
        }

        fun create(): FilePicker {
            val filePicker = FilePicker()
            filePicker.pickParams = pickParams
            return filePicker
        }

    }

    fun open(activity:Activity) {
        val i = Intent(activity, FileListActivity::class.java)
        activity.startActivityForResult(i, REQUEST_CODE)
    }


}
package com.fans.fmanage.ui

import android.content.Intent
import android.util.Log
import android.view.WindowManager
import com.fans.fmanage.R
import com.fans.fmanage.core.FilePicker
import com.fans.fmanage.databinding.ActivityMainBinding
import com.fans.fmanage.utils.AppConfig
import com.fans.fmanage.viewmodel.MainViewModel

class TestActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun initView() {
        Log.d(TAG,"initView")
        mBinding.model = mViewModel
    }

    override fun initEvent() {
        Log.d(TAG,"initEvent")
        //Android11存储
        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
             val intent =  Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
             startActivityForResult(intent,2);
         }else{

         }*/

        mViewModel.liveData.observe(this){
            FilePicker
                .build()
                .buildPath(AppConfig.ROOT_PATH+"/Android")
                .buildFileSupport()
                .buildFileSupport()
                .create()
                .open(this)
        }
    }

    override fun initData() {
        Log.d(TAG,"initData")
    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG,data?.getStringExtra(FilePicker.KEY_DATA)?:"")
    }
}
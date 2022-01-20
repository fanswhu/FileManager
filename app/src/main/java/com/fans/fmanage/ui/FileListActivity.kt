package com.fans.fmanage.ui

import android.animation.ObjectAnimator
import android.util.Log
import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fans.fmanage.R
import com.fans.fmanage.databinding.ActivityFileListBinding
import com.fans.fmanage.utils.AppConfig
import com.fans.fmanage.viewmodel.FileListViewModel


class FileListActivity : BaseActivity<ActivityFileListBinding, FileListViewModel>() {
    override fun getLayout(): Int {
        return R.layout.activity_file_list
    }

    override fun initView() {
        requestPermissions(
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            AppConfig.REQUEST_PERMISSION
        )

        mBinding.fileRv.post {
            mBinding.container.minHeight = mBinding.fileRv.height + 5000
            mBinding.container.requestLayout()
        }
    }

    override fun initEvent() {
        mViewModel.selectLiveData.observe(this) {
            Log.d(TAG, "选择了$it 目录")
            finish()
        }
        mViewModel.backLiveData.observe(this) {
            Log.d(TAG, "偏移$it 距离")
            if (it == null) {
                finish()
            } else {
                mBinding.fileRv.postDelayed({ mBinding.fileRv.scrollBy(0, it) }, 10)
            }
        }
        mViewModel.stateLiveData.observe(this) {
            Log.d(TAG, "state$it")
            when (it) {
                FileListViewModel.SHOW_LOADING -> showLoading()
                FileListViewModel.SHOW_CONTENT -> showContent()
                FileListViewModel.SHOW_EMPTY -> showEmpty()
                FileListViewModel.SHOW_ERROR -> showError()
            }
        }

        mBinding.fileRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.d(TAG, "dy:$dy")
                mViewModel.saveScrollDiff(dy)
            }
        })

    }

    private fun showError() {
        Log.d(TAG, "showError")
    }

    private fun showEmpty() {
        Log.d(TAG, "showEmpty")
        initLayout()
        if (mBinding.layoutEmpty.isInflated) {
            mBinding.layoutEmpty.root.visibility = View.VISIBLE
        }else{
            val v = mBinding.layoutEmpty.viewStub?.inflate()
        }


    }

    private fun showContent() {
        Log.d(TAG, "showContent")
        initLayout()
    }

    private fun showLoading() {
        initLayout()
        Log.d(TAG, "showLoading")
        if (mBinding.layoutLoading.isInflated) {
            mBinding.layoutLoading.root.visibility = View.VISIBLE
        }else{
            val v = mBinding.layoutLoading.viewStub?.inflate()
            val img = v?.findViewById<ImageView>(R.id.iv_loading)
            val anim = ObjectAnimator.ofFloat(img, "rotation", 0f, 360f)
            anim.repeatCount = -1
            anim.duration = (1500)
            anim.start()
        }

    }

    override fun initData() {
        mBinding.model = mViewModel
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == AppConfig.REQUEST_PERMISSION) {
            Log.d(TAG, "permissions storage request success")
            val layoutManager = LinearLayoutManager(this)
            mBinding.fileRv.layoutManager = layoutManager
            mBinding.fileRv.adapter = mViewModel.mAdapter
        }

    }

    override fun onBackPressed() {
        mViewModel.onBack(false)
    }

    fun initLayout() {
        mBinding.layoutLoading.root?.visibility = View.GONE

        mBinding.layoutEmpty.root?.visibility = View.GONE
    }

}
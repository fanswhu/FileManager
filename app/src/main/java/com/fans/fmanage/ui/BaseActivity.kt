package com.fans.fmanage.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fans.fmanage.viewmodel.ShareViewModel

import java.lang.reflect.ParameterizedType

abstract class BaseActivity<T : ViewDataBinding, M : AndroidViewModel> : FragmentActivity() {
    protected lateinit var mBinding: T
    lateinit var mViewModel: M
    private lateinit var type: Class<M>
    val TAG by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
        javaClass.simpleName
    }


    init {
        val superClass = this.javaClass.genericSuperclass
        val parameterizedType = superClass as ParameterizedType
        val q = parameterizedType.actualTypeArguments[1]
        type = q as Class<M>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView<T>(this, getLayout());
        mViewModel = ViewModelProvider(this, defaultViewModelProviderFactory)
            .get(type)
        mBinding.lifecycleOwner = this

        initView()

        initEvent()

        initData()
    }

    abstract fun getLayout(): Int

    abstract fun initView()

    abstract fun initEvent()

    abstract fun initData()
}




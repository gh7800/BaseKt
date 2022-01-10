package cn.shineiot.base

import android.annotation.SuppressLint
import androidx.viewbinding.ViewBinding
import cn.shineiot.base.databinding.ActivityMainBinding
import cn.shineiot.base.mvp.BaseMVPActivity
import cn.shineiot.base.mvvm.BaseVmActivity
import kotlinx.coroutines.launch

class MainActivity : BaseVmActivity<MainViewModel>() {
    lateinit var viewBinding : ActivityMainBinding

    override fun getBinding(): ViewBinding {
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        return viewBinding
    }

    override fun viewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        viewBinding.textView.text = "Hello-1-1"
    }

}
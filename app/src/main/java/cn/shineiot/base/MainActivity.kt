package cn.shineiot.base

import androidx.viewbinding.ViewBinding
import cn.shineiot.base.databinding.ActivityMainBinding
import cn.shineiot.base.mvp.BaseMVPActivity
import cn.shineiot.base.mvp.BaseView

class MainActivity : BaseMVPActivity<MainView,MainPresenter>() {
    lateinit var viewBinding : ActivityMainBinding

    override fun getBinding(): ViewBinding {
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        return viewBinding
    }

    override fun initPresenter(): MainPresenter {
        return MainPresenter()
    }

    override fun initView() {
        viewBinding.textView.text = "Hello"
    }
/*
    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun errorMsg(msg: String?) {

    }*/
}
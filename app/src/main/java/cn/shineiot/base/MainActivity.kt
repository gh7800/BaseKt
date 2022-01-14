package cn.shineiot.base

import android.annotation.SuppressLint
import android.view.View
import cn.shineiot.base.databinding.ActivityMainBinding
import cn.shineiot.base.databinding.LayoutToolbarBinding
import cn.shineiot.base.mvvm.BaseVmActivity
import cn.shineiot.base.utils.ActManager
import cn.shineiot.base.utils.DialogUtil
import com.maning.mndialoglibrary.MToast

class MainActivity : BaseVmActivity<ActivityMainBinding,MainViewModel>() {
    private lateinit var layoutToolbarBinding : LayoutToolbarBinding

    override fun viewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        layoutToolbarBinding = LayoutToolbarBinding.bind(viewBinding.root) //获取toolBarBinding

        setToolBar(layoutToolbarBinding.toolbar,"首页",layoutToolbarBinding.toolbarTitle)

        viewBinding.textView.text = "Hello-1-1"

        layoutToolbarBinding.toolbar.navigationIcon = null

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val homeFragment = HomeFragment()
        transaction.replace(R.id.frameLayout,homeFragment).commitNow()


    }

    fun click(v : View){

        ActManager.start(LoginActivity::class.java)
    }


    override fun dismissDialog() {
        DialogUtil.showLoading(mContext)
    }

    override fun showDialog() {
        DialogUtil.hideDialog()
    }

}
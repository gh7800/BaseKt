package cn.shineiot.basic

import android.annotation.SuppressLint
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cn.shineiot.base.mvvm.BaseVmActivity
import cn.shineiot.base.utils.ActManager
import cn.shineiot.base.utils.DialogUtil
import cn.shineiot.basic.databinding.ActivityMainBinding
import cn.shineiot.basic.databinding.LayoutToolbarBinding
import cn.shineiot.basic.homeFragment.HomeFragment

class MainActivity : BaseVmActivity<ActivityMainBinding, MainViewModel>() {
    private lateinit var layoutToolbarBinding : LayoutToolbarBinding

    override fun viewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        setTheme(R.style.ISplashTheme)
        installSplashScreen()

        layoutToolbarBinding = LayoutToolbarBinding.bind(viewBinding.root) //获取toolBarBinding
        //StatusBarUtil.setColor(mContext,ContextCompat.getColor(mContext,R.color.white))

        setToolBar(layoutToolbarBinding.toolbar,"首页",layoutToolbarBinding.toolbarTitle,false)

        viewBinding.textView.text = "Hello-1-1"

        //layoutToolbarBinding.toolbar.navigationIcon = null

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val homeFragment = HomeFragment()
        transaction.replace(R.id.frameLayout,homeFragment).commitNow()


    }

    fun click(v : View){
        ActManager.start(LoginActivity::class.java)
    }


    override fun dismissDialog(msg : String?) {
        DialogUtil.showLoading(mContext)
    }

    override fun showDialog() {
        DialogUtil.hideDialog()
    }

}
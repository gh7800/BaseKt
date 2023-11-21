package cn.shineiot.basic

import android.annotation.SuppressLint
import android.view.View
import cn.shineiot.base.mvvm.BaseMvvmActivity
import cn.shineiot.base.mvvm.initViewModelS
import cn.shineiot.base.utils.ActManager
import cn.shineiot.base.utils.DialogUtil
import cn.shineiot.basic.databinding.ActivityMainBinding
import cn.shineiot.basic.databinding.LayoutToolbarBinding
import cn.shineiot.basic.homeFragment.HomeFragment
import cn.shineiot.basic.login.LoginActivity

class MainActivity : BaseMvvmActivity<ActivityMainBinding>() {

    private val viewModel by lazy {
        initViewModelS(mContext,MainViewModel::class,MainRepository::class)
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        setToolBar(viewBinding.bar.toolbar,"首页",viewBinding.bar.toolbarTitle,false)

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
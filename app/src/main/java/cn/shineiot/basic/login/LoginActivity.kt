package cn.shineiot.basic.login

import cn.shineiot.base.mvvm.BaseMvvmActivity
import cn.shineiot.base.mvvm.Status
import cn.shineiot.base.mvvm.initViewModelS
import cn.shineiot.base.utils.DialogUtil
import cn.shineiot.base.utils.LogUtil
import cn.shineiot.base.utils.MMKVUtil
import cn.shineiot.base.utils.SnackBarUtil
import cn.shineiot.basic.bean.UserBean
import cn.shineiot.basic.databinding.ActivityLoginBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @Description login
 * @Author : GF63
 * @Date : 2022/1/12 16:36
 */
class LoginActivity : BaseMvvmActivity<ActivityLoginBinding>() {
    private var loginJob : Job ?= null

    private val viewModel by lazy {
        initViewModelS(this, LoginViewModel::class, LoginRepository::class)
    }

    override fun initView() {
        setToolBar(viewBinding.tools.toolbar, "login", viewBinding.tools.toolbarTitle)

        MMKVUtil.save("title",UserBean("username"))
        viewBinding.loginEvent.setOnClickListener {
            loginJob = launch {
                viewModel.login("admin", "admin", "", "1").collect {
                    when (it.status) {
                        Status.LOADING -> showDialog()
                        Status.SUCCESS -> {
                            delay(5000)
                            dismissDialog("登录成功")
                            val userbean = MMKVUtil.getParcelable("title",UserBean::class.java)
                           LogUtil.e(userbean.toString())
                        }
                        Status.ERROR -> {
                            dismissDialog(it.message)
                        }
                    }
                }
            }
        }

    }

    override fun observe() {
        super.observe()

    }

    override fun dismissDialog(msg: String?) {
        DialogUtil.hideDialog()
        SnackBarUtil.show(viewBinding.loginEvent, msg)
    }

    override fun showDialog() {
        DialogUtil.showLoading(mContext,"正在登录.") {
            viewModel.cancelJob(loginJob)
            SnackBarUtil.show(viewBinding.loginEvent,"已取消")
        }
    }
}
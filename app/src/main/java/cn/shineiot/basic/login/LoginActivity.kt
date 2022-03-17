package cn.shineiot.basic.login

import cn.shineiot.base.mvvm.BaseMvvmActivity
import cn.shineiot.base.mvvm.Status
import cn.shineiot.base.mvvm.initViewModelS
import cn.shineiot.base.utils.DialogUtil
import cn.shineiot.base.utils.SnackBarUtil
import cn.shineiot.basic.databinding.ActivityLoginBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @Description login
 * @Author : GF63
 * @Date : 2022/1/12 16:36
 */
class LoginActivity : BaseMvvmActivity<ActivityLoginBinding>() {

    private val viewModel by lazy {
        initViewModelS(this, LoginViewModel::class, LoginRepository::class)
    }

    override fun initView() {
        setToolBar(viewBinding.tools.toolbar, "login", viewBinding.tools.toolbarTitle)

        viewBinding.loginEvent.setOnClickListener {
            launch {
                viewModel.login("admin", "admin", "", "1").collect {
                    when (it.status) {
                        Status.LOADING -> showDialog()
                        Status.SUCCESS -> {
                            dismissDialog("登录成功")
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
        DialogUtil.showLoading(mContext)
    }
}
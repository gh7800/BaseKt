package cn.shineiot.base

import cn.shineiot.base.databinding.ActivityLoginBinding
import cn.shineiot.base.databinding.LayoutToolbarBinding
import cn.shineiot.base.mvvm.BaseVmActivity
import cn.shineiot.base.utils.DialogUtil
import cn.shineiot.base.utils.ToastUtil

/**
 * @Description login
 * @Author : GF63
 * @Date : 2022/1/12 16:36
 */
class LoginActivity : BaseVmActivity<ActivityLoginBinding,LoginViewModel>(){
    private lateinit var toolBinding : LayoutToolbarBinding

    override fun viewModelClass(): Class<LoginViewModel> {
        return LoginViewModel::class.java
    }

    override fun initView() {
        toolBinding = LayoutToolbarBinding.bind(viewBinding.root)
        setToolBar(toolBinding.toolbar,"login",toolBinding.toolbarTitle)

        viewBinding.tv.text = "login"

    }

    override fun observe() {
        super.observe()

        mViewModel
    }

    override fun dismissDialog(msg : String?) {
        DialogUtil.showLoading(mContext)
    }

    override fun showDialog() {
        DialogUtil.hideDialog()
    }
}
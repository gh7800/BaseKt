package cn.shineiot.base

import cn.shineiot.base.databinding.FragmengHomeBinding
import cn.shineiot.base.mvvm.BaseVmFragment
import cn.shineiot.base.utils.ActManager
import cn.shineiot.base.utils.DialogUtil

/**
 * @Description TODO
 * @Author : GF63
 * @Date : 2022/1/13 16:44
 */
class HomeFragment :BaseVmFragment<FragmengHomeBinding,HomeViewModel>(){
    override fun viewModelClass(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun initView() {
        viewBinding.back.setOnClickListener {
            ActManager.start(LoginActivity::class.java)
        }

        viewBinding.back.text = "fragment-1"

    }

    override fun dismissDialog(msg : String?) {
    }

    override fun showDialog() {
    }

}
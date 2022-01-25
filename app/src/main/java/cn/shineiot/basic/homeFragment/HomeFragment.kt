package cn.shineiot.basic.homeFragment

import cn.shineiot.base.mvvm.BaseVmFragment
import cn.shineiot.base.utils.ActManager
import cn.shineiot.basic.LoginActivity
import cn.shineiot.basic.R
import cn.shineiot.basic.databinding.FragmengHomeBinding

/**
 * @Description TODO
 * @Author : GF63
 * @Date : 2022/1/13 16:44
 */
class HomeFragment : BaseVmFragment<FragmengHomeBinding, HomeViewModel>(){

    private val adapter by lazy { HomeAdapter(R.layout.item_home) }
    private val list : MutableList<String> = arrayListOf("登录")

    override fun viewModelClass(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    override fun initView() {
        viewBinding.back.setOnClickListener {
            ActManager.start(LoginActivity::class.java)
        }

        viewBinding.back.text = "fragment-1"

        viewBinding.recyclerview.adapter = adapter
        adapter.setData(list)

    }

    override fun dismissDialog(msg : String?) {
    }

    override fun showDialog() {
    }

}
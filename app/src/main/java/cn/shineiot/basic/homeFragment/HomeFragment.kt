package cn.shineiot.basic.homeFragment

import android.view.View
import cn.shineiot.base.adapter.BaseAdapter
import cn.shineiot.base.adapter.BaseAdapterVD
import cn.shineiot.base.mvvm.BaseMvvmFragment
import cn.shineiot.base.mvvm.initViewModelS
import cn.shineiot.base.utils.ActManager
import cn.shineiot.base.utils.SnackBarUtil
import cn.shineiot.basic.login.LoginActivity
import cn.shineiot.basic.R
import cn.shineiot.basic.databinding.HomeFragmentBinding
import kotlinx.coroutines.launch

/**
 * @Description home
 * @Author : GF63
 * @Date : 2022/1/13 16:44
 */
class HomeFragment : BaseMvvmFragment<HomeFragmentBinding>(){
    private val viewModel by lazy {
        initViewModelS(this,HomeViewModel::class,HomeRepository::class)
    }
    private val adapter by lazy { HomeAdapter() }
    private val list : MutableList<String> = arrayListOf("登录")

    override fun initView() {
        viewBinding.back.setOnClickListener {
            ActManager.start(LoginActivity::class.java)
        }

        viewBinding.back.text = "fragment-1"

        viewBinding.recyclerview.adapter = adapter
        adapter.setData(list)

        adapter.setItemClickListener(object : BaseAdapterVD.ItemClick{
            override fun OnItemClick(v: View, position: Int) {
                if(v.id == R.id.title){
                    SnackBarUtil.show(viewBinding.recyclerview,adapter.getItem(position))
                    ActManager.start(LoginActivity::class.java)
                }else{
                    SnackBarUtil.show(viewBinding.recyclerview,"item")
                }
            }
        })

        launch {

        }
    }

    override fun dismissDialog(msg : String?) {
    }

    override fun showDialog() {
    }

}
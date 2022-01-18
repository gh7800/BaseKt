package cn.shineiot.base.mvvm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import cn.shineiot.base.binding.ViewBindingCreator
import cn.shineiot.base.utils.LogUtil

/**
 * BaseMVVMFragment
 */
abstract class BaseVmFragment<VB : ViewBinding,VM : BaseViewModel> : Fragment() {
    protected abstract fun viewModelClass(): Class<VM>
    protected abstract fun initView()
    open fun observe() {}
    protected abstract fun showDialog()
    protected abstract fun dismissDialog(msg : String?)

    protected lateinit var mContext: AppCompatActivity
    protected lateinit var mViewModel: VM
    protected lateinit var viewBinding: VB

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewBinding = ViewBindingCreator.create(javaClass,layoutInflater,container)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogUtil.e(this.javaClass.simpleName+"---onViewCreated")
        initViewModel()
        initView()
        observe()
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(viewModelClass())
    }

    override fun onResume() {
        super.onResume()
        LogUtil.e(this.javaClass.simpleName+"---onResume")
    }

    override fun onPause() {
        super.onPause()
        LogUtil.e(this.javaClass.simpleName+"---onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.e(this.javaClass.simpleName+"---onPause")
    }
}

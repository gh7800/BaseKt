package cn.shineiot.base.mvvm

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseVmFragment<VM : BaseViewModel> : Fragment() {
    lateinit var mContext: Activity

    protected open lateinit var mViewModel: VM
    private var viewBinding : ViewBinding ? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewBinding = getBinding()
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
        observe()
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(viewModelClass())
    }

    protected abstract fun getBinding(): ViewBinding
    protected abstract fun viewModelClass(): Class<VM>
    protected abstract fun initView()

    open fun observe() {

    }

    override fun onDestroy() {
        super.onDestroy()

        viewBinding = null
    }
}

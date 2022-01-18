@file:Suppress("UNCHECKED_CAST")

package cn.shineiot.base.mvp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import cn.shineiot.base.utils.LogUtil

/**
 * @author Xuhao
 * created: 2017/10/25
 * desc:
 */

abstract class BaseFragment<V : BaseView, T : BasePresenter<V>> : Fragment() {

    /**
     * 视图是否加载完毕
     */
    private var isViewPrepare = true
    private lateinit var mContext : Context

    /**
     * 数据是否加载过了
     */
    private var hasLoadData = false

    private var viewBinding : ViewBinding?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding =getBinding()
        return viewBinding?.root
    }

    override fun onResume() {
        super.onResume()
        LogUtil.e(this.javaClass.simpleName+"---onResume")
        lazyLoad()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogUtil.e(this.javaClass.simpleName+"---onViewCreated")

        initView()
    }

    /**
     * 获取 ViewBinding
     */
    abstract fun getBinding(): ViewBinding

    /**
     * 初始化 ViewI
     */
    abstract fun initView()

    /**
     * 懒加载
     */
    abstract fun lazyLoad()

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.e(this.javaClass.simpleName+"---onDestroy")

        viewBinding = null
    }

}

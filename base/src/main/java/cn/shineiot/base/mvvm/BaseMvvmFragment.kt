package cn.shineiot.base.mvvm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import cn.shineiot.base.binding.ViewBindingCreator
import cn.shineiot.base.utils.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * BaseMVVMFragment
 */
abstract class BaseMvvmFragment<VB : ViewDataBinding> : Fragment() , CoroutineScope {
    abstract fun initView()
    open fun showDialog(){}
    open fun dismissDialog(msg : String? = null){}
    open fun observe() {}

    protected lateinit var mContext: AppCompatActivity
    protected lateinit var viewBinding: VB

    //job用于控制协程,后面launch{}启动的协程,返回的job就是这个job对象
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private var resultCallBack: Deque<(ActivityResult) -> Unit> = ArrayDeque()
    private val activityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            resultCallBack.pop()?.let {
                it(result)
            }
        }

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

        job = Job()
        initView()
        observe()
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
        LogUtil.e(this.javaClass.simpleName+"---onDestroy")
    }

}

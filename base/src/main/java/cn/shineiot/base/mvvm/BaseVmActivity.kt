package cn.shineiot.base.mvvm

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import cn.shineiot.base.binding.ViewBindingCreator
import cn.shineiot.base.utils.ActManager
import cn.shineiot.base.utils.LogUtil
import cn.shineiot.base.utils.ToastUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * Base-Mvvm-Activity
 */
abstract class BaseVmActivity<VB : ViewBinding,VM : BaseViewModel> : AppCompatActivity(),CoroutineScope {
    protected abstract fun viewModelClass(): Class<VM>
    protected abstract fun initView()

    protected abstract fun showDialog()
    protected abstract fun dismissDialog(msg : String?)

    open fun observe() {}
    open fun initData() {}

    protected lateinit var mContext: AppCompatActivity
    protected lateinit var viewBinding : VB
    protected lateinit var mViewModel: VM

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ViewBindingCreator.create(javaClass,layoutInflater)
        setContentView(viewBinding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //设置竖屏

        job = Job()
        mContext = this
        ActManager.addActivity(mContext)

        initViewModel()
        initView()
        observe()
        // 因为Activity恢复后savedInstanceState不为null，
        // 重新恢复后会自动从ViewModel中的LiveData恢复数据，
        // 不需要重新初始化数据
        if (savedInstanceState == null) {
            initData()
        }
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(viewModelClass())
    }

    fun setToolBar(toolbar: Toolbar, title: String?, textView: AppCompatTextView?) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayUseLogoEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)

        textView?.text = title
    }

    /**
     * 封装startActivityForResult
     */
    protected fun startActivityForResult(
        cls: Class<*>,
        block: Intent.() -> Unit = {},
        callBack: (result: ActivityResult) -> Unit
    ) {
        resultCallBack.push(callBack)
        activityForResult.launch(Intent(this, cls).apply(block))
    }

    /**
     * 打开软键盘
     */
    fun openKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /**
     * 关闭软键盘
     */
    fun closeKeyBord(mEditText: EditText) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
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

        ActManager.finishActivity(mContext)
    }

}

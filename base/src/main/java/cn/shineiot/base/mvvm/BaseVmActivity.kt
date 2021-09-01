package cn.shineiot.base.mvvm

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import cn.shineiot.base.utils.ActManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseVmActivity<VM : BaseViewModel> : AppCompatActivity(),CoroutineScope {
    lateinit var mContext: FragmentActivity

    protected open lateinit var mViewModel: VM
    //job用于控制协程,后面launch{}启动的协程,返回的job就是这个job对象
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getBinding().root)

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

    fun setToolbar(toolbar: Toolbar, title: String?, textView: AppCompatTextView?) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayUseLogoEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)

        textView?.text = title
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(viewModelClass())
    }

    protected abstract fun getBinding(): ViewBinding
    protected abstract fun viewModelClass(): Class<VM>
    protected abstract fun initView()

    open fun observe() {}
    open fun initData() {}

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
        ActManager.finish(mContext)
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        ActManager.finishActivity(mContext)
    }

}

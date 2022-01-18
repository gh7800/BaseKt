package cn.shineiot.basic.mvp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import cn.shineiot.basic.utils.ActManager
import cn.shineiot.basic.utils.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


/**
 * BaseMVPActivity基类
 */
abstract class BaseMVPActivity<V : BaseView, T : BasePresenter<V>> : AppCompatActivity(),CoroutineScope {
    lateinit var mContext: AppCompatActivity
    lateinit var presenter: T

    //job用于控制协程,后面launch{}启动的协程,返回的job就是这个job对象
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun setToolbar(toolbar: Toolbar){
        this.setToolbar(toolbar,null,null)
    }

    @SuppressLint("RestrictedApi")
    fun setToolbar(toolbar: Toolbar, title: String?,textView: AppCompatTextView?) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayUseLogoEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)

        textView?.text = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getBinding().root)

        job = Job()

        mContext = this
        ActManager.addActivity(mContext)
        initP()
        initView()
    }

    abstract fun getBinding(): ViewBinding

    /**
     * 初始化presenter
     */
    abstract fun initPresenter(): T

    /**
     * 初始化 View
     */
    abstract fun initView()

    @Suppress("UNCHECKED_CAST")
    fun initP() {
        presenter = initPresenter()
        if(this is BaseView) {
            presenter.attachView(this as V)
        }
    }

    override fun onResume() {
        super.onResume()
        LogUtil.e(this.javaClass.simpleName+"---onResume")
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
        ActManager.finish(mContext)
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.e(this.javaClass.simpleName+"---onDestroy")

        job.cancel()
        presenter.detachView()
        ActManager.finishActivity(mContext)
    }

}



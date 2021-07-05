package cn.shineiot.base.mvp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import cn.shineiot.base.utils.ActManager
import cn.shineiot.base.utils.LogUtil


/**
 * BaseActivity基类
 */
abstract class BaseActivity : AppCompatActivity() {
     lateinit var mContext: AppCompatActivity

    fun  setToolbar(toolbar: Toolbar){
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

        textView?.text = title
        actionBar?.setHomeButtonEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getBinding().root)

        mContext = this
        ActManager.addActivity(mContext)
        initView()
    }

    /**
     * 获取 ViewBinding
     */
    abstract fun getBinding(): ViewBinding

    /**
     * 初始化 View
     */
    abstract fun initView()

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
    fun closeKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.e(this.javaClass.simpleName+"---onDestroy")
        ActManager.finishActivity(mContext)
    }

}



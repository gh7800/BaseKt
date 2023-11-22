package cn.shineiot.base.mvvm

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import cn.shineiot.base.binding.ViewBindingCreator
import cn.shineiot.base.utils.ActManager
import cn.shineiot.base.utils.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.coroutines.CoroutineContext

/**
 * Base-Mvvm-Activity
 * viewBinding
 */
abstract class BaseMvvmActivity<VB : ViewDataBinding> : AppCompatActivity(),
    CoroutineScope {

    //必须被重写
    abstract fun initView()
    //可以被重写
    protected open fun showDialog(){}
    protected open fun dismissDialog(msg: String ?= null){}
    protected open fun observe() {}
    protected open fun initData() {}

    protected lateinit var mContext: AppCompatActivity
    protected lateinit var viewBinding: VB

    //job用于控制协程,后面launch{}启动的协程,返回的job就是这个job对象
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val activityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult(result)
        }
    private val activityForResultTakePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
            onActivityResultForTakePicture(result)
        }
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){
        if (it != null) {
            onActivityResultForGetContent(it)
        }
    }
    private val getContentS = registerForActivityResult(ActivityResultContracts.GetMultipleContents()){
        onActivityResultForGetContentS(it)
    }


    protected open fun onActivityResult(result : ActivityResult){}
    protected open fun onActivityResultForTakePicture(result : Boolean){}
    protected open fun onActivityResultForGetContent(uri: Uri){}
    protected open fun onActivityResultForGetContentS(uris : List<Uri>){}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ViewBindingCreator.create(javaClass, layoutInflater)
        setContentView(viewBinding.root)

        job = Job()
        mContext = this
        ActManager.addActivity(mContext)

        initView()
        observe()
        // 因为Activity恢复后savedInstanceState不为null，
        // 重新恢复后会自动从ViewModel中的LiveData恢复数据，
        // 不需要重新初始化数据
        if (savedInstanceState == null) {
            initData()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setToolBar(toolbar: Toolbar, title: String?, textView: AppCompatTextView?, isBack : Boolean = true, icon : Int = 0) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayUseLogoEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)

        textView?.text = title

        if(!isBack){
            toolbar.navigationIcon = null
        }
        if(icon != 0){
            toolbar.navigationIcon = mContext.getDrawable(icon)
        }
    }

    //startActivity
    protected open fun startActivityForResult(
        cls: Class<*>,
        block: Intent.() -> Unit = {}
    ) {
        activityForResult.launch(Intent(this, cls).apply(block))
    }
    //打开相机
    protected open fun startActivityTakePicture(uri : Uri){
        activityForResultTakePicture.launch(uri)
    }
    //获取单个图片或文件,image/*
    protected open fun startActivityGetContent(type : String){
        getContent.launch(type)
    }
    //获取多个图片或文件
    protected open fun startActivityGetContentS(type : String){
        getContentS.launch(type)
    }

    override fun getResources(): Resources? {
        val res = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.displayMetrics)
        return res
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
        LogUtil.e(this.javaClass.simpleName + "---onResume")
    }

    override fun onPause() {
        super.onPause()
        LogUtil.e(this.javaClass.simpleName + "---onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.e(this.javaClass.simpleName + "---onDestroy")

        ActManager.finishActivity(mContext)
    }

}

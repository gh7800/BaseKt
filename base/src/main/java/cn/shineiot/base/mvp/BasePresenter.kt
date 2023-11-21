package cn.shineiot.base.mvp

import cn.shineiot.base.bean.LoginEvent
import cn.shineiot.base.utils.*
import com.google.gson.JsonParseException
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

typealias Block<T> = suspend () -> T
typealias Error = suspend (e: Exception) -> Unit
typealias Cancel = suspend (e: Exception) -> Unit

open class BasePresenter<T : BaseView> : CoroutineScope, IPresenter<T> {
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    var mView: T? = null

    override fun attachView(mRootView: T) {
        this.mView = mRootView
        job = Job()
    }

    override fun detachView() {
        mView = null
        job.cancel()
    }

    private val isViewAttached: Boolean
        get() = mView != null

    fun checkViewAttached() {
        if (!isViewAttached) throw MvpViewNotAttachedException()
    }

    private class MvpViewNotAttachedException internal constructor() :
        RuntimeException("Please call IPresenter.attachView(IBaseView) before" + " requesting data to the IPresenter")

    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @param error 错误时执行
     * @return Job
     */
    protected fun launch(block: Block<Unit>, error: Error? = null, cancel: Cancel? = null): Job {
        if (!NetworkUtil.isNetworkConnected()) {
            val exc = Exception("网络连接失败,请检查网络?")
            return launch(Dispatchers.Main) {
                error?.invoke(exc)
            }
        }

        return launch(Dispatchers.Main) {
            try {
                block.invoke()
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> {
                        cancel?.invoke(e)
                    }
                    else -> {
                        val exception = onError(e)
                        error?.invoke(exception)
                    }
                }
            }
        }
    }

    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @return Deferred<T>
     */
    protected fun <T> async(block: Block<T>): Deferred<T> {
        return async { block.invoke() }
    }

    /**
     * 取消协程
     * @param job 协程job
     */
    fun cancelJob() {
        if (job.isActive && !job.isCompleted && !job.isCancelled) {
            job.cancel()
        }
    }

    /**
     * 统一处理错误
     * @param e 异常
     */
    private fun onError(e: Exception): java.lang.Exception {
        LogUtil.e("onError---" + e.message)
        var errorMsg: String? = null
        when (e) {
            is ApiException -> {
                when (e.message) {
                    "权限验证错误" -> {
                        // 登录失败
                        errorMsg = "权限验证错误"
                        EventBus.getDefault().post(LoginEvent())
                    }
                    else -> {
                        // 其他错误
                        LogUtil.e("其他错误___" + e.code + "_" + e.message)
                        errorMsg = e.message
                    }
                }
            }
            is ConnectException -> {
                // 连接失败
                errorMsg = "网络连接失败"
            }
            is SocketTimeoutException -> {
                // 请求超时
                errorMsg = "请求超时"

            }
            is JsonParseException -> {
                // 数据解析错误
                errorMsg = "数据解析错误"

            }
            is UnknownHostException -> {
                errorMsg = "网络连接异常,请检查网络?"

            }
            is HttpException -> {
                when (e.code()) {
                    400 -> errorMsg = "客户端请求的语法错误"
                    401 -> {
                        errorMsg = "权限验证失败"
                        EventBus.getDefault().post(LoginEvent())
                    }
                    403 -> errorMsg = "访问已被禁止"
                    404 -> {
                        errorMsg = "找不到请求的网页"
                    }
                    405 -> {
                        errorMsg = "不允许此方法"
                    }
                    500 -> {
                        errorMsg = "服务器的错误"
                    }
                    else -> {
                        errorMsg = "请求错误"
                        LogUtil.e("http--" + e.message)
                    }
                }
            }
            else -> {
                // 其他错误
                errorMsg = "请求失败"
                //LogUtil.e("其他错误---------" + e.message)
            }
        }
        return Exception(errorMsg)
    }

}
package cn.shineiot.base.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.shineiot.base.bean.LoginEvent
import cn.shineiot.base.mvp.ApiException
import cn.shineiot.base.utils.LogUtil
import com.google.gson.JsonParseException
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

typealias Block<T> = suspend () -> T
typealias Error = suspend (e: Exception) -> Unit
typealias Cancel = suspend (e: Exception) -> Unit

open class BaseViewModel : ViewModel() {

    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @param error 错误时执行
     * @return Job
     */
    protected fun launch(block: Block<Unit>, error: Error? , cancel: Cancel?): Job {
        return viewModelScope.launch {
            try {
                block.invoke()
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> {
                        cancel?.invoke(e)
                    }
                    else -> {
                        onError(e)
                        error?.invoke(e)
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
        return viewModelScope.async { block.invoke() }
    }

    /**
     * 取消协程
     * @param job 协程job
     */
    protected fun cancelJob(job: Job?) {
        if (job != null && job.isActive && !job.isCompleted && !job.isCancelled) {
            job.cancel()
        }
    }

    /**
     * 未使用
     *
     * 统一处理错误
     * @param e 异常
     */
    protected fun onError(e: Exception) : String? {
        LogUtil.e("onError---"+e.message)
        var error : String? = null

        when (e) {
            is ApiException -> {
                error = when (e.message) {
                    "权限验证错误" -> {
                        EventBus.getDefault().post(LoginEvent())
                        "权限验证错误"
                    }
                    else -> {
                        LogUtil.e("错误___" + e.code + "_" + e.message)
                        e.message
                    }
                }
            }
            is ConnectException -> {
                error = "网络连接失败"
            }
            is SocketTimeoutException -> {
                error = "网络请求超时"
            }
            is JsonParseException -> {
                error = "数据解析错误"
            }
            is UnknownHostException -> {
                error = "网络未打开,无法解析域名"
            }
            is HttpException -> {
                when (e.code()) {
                    401 -> {
                        error = "权限验证错误"
                        EventBus.getDefault().post(LoginEvent())
                    }
                    403 -> error = "访问已被禁止"
                    404 -> error = "找不到请求的网页"
                    405 -> error = "请求方法不对"
                    500 -> error = "服务器遇到错误"
                    else -> {
                        error = e.message()
                        LogUtil.e("http错误--" + e.message)
                    }
                }
            }
            else -> {
                LogUtil.e("其他错误---------" + e.message)
                error = "${e.message}"
            }
        }
        return error
    }

}
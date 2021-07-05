package cn.shineiot.base.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.shineiot.base.bean.LoginEvent
import cn.shineiot.base.mvp.ApiException
import cn.shineiot.base.utils.LogUtil
import cn.shineiot.base.utils.ToastUtil
import cn.shineiot.base.utils.ToastUtils
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

    //protected val userRepository by lazy { UserRepository() }

    val loginStatusInvalid: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @param error 错误时执行
     * @return Job
     */
    protected fun launch(block: Block<Unit>, error: Error? = null, cancel: Cancel? = null): Job {
        return viewModelScope.launch {
            try {
                block.invoke()
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> {
                        cancel?.invoke(e)
                    }
                    else -> {
                        error?.invoke(e)
                        onError(e)
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
     * 统一处理错误
     * @param e 异常
     */
    private fun onError(e: Exception) {
        //LogUtil.e("onError---"+e.message)
        when (e) {
            is ApiException -> {
                when (e.message) {
                    "权限验证错误" -> {
                        // 登录失效
                        EventBus.getDefault().post(LoginEvent())
                    }
                    else -> {
                        // 其他错误
                        LogUtil.e("错误___" + e.code + "_" + e.message)
                    }
                }
            }
            is ConnectException -> {
                // 连接失败
                LogUtil.e(e.message)
                ToastUtil.show("网络连接失败")
            }
            is SocketTimeoutException -> {
                // 请求超时
                LogUtil.e(e.message)
                ToastUtil.show("请求超时")
            }
            is JsonParseException -> {
                // 数据解析错误
                LogUtil.e(e.message)
                ToastUtil.show("数据解析错误")
            }
            is UnknownHostException -> {
                LogUtil.e(e.message)
                ToastUtil.show("网络未打开")
            }
            is HttpException -> {
                when (e.code()) {
                    404 -> ToastUtil.show("找不到请求的网页")
                    405 -> ToastUtil.show("请求方法不对")
                    500 -> ToastUtil.show("服务器遇到错误")
                    else -> LogUtil.e("http错误--" + e.message)
                }
            }
            else -> {
                // 其他错误
                LogUtil.e("其他错误---------" + e.message)
            }
        }
    }

    /**
     * 登录状态
     */
    //fun loginStatus() = userRepository.isLogin()

}
package cn.shineiot.base.mvvm

import cn.shineiot.base.bean.LoginEvent
import cn.shineiot.base.mvp.ApiException
import cn.shineiot.base.utils.Constants
import cn.shineiot.base.utils.LogUtil
import cn.shineiot.base.utils.MMKVUtil
import com.google.gson.JsonParseException
import org.greenrobot.eventbus.EventBus
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.Exception

/**
 *    author : kpa
 *    e-mail : billkp@yeah.net
 *    将网络调用的状态传给UI层
 */
open class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?) = Resource(Status.SUCCESS, data, null)
        fun <T> error(e: Exception, data: T? = null) = Resource(Status.ERROR,data, onError(e))
        fun <T> loading(data: T? = null) = Resource(Status.LOADING, data, null)

        /**
         * 统一处理错误
         * @param e 异常
         */
        protected fun onError(e: Exception) : String? {
            LogUtil.e("Resource onError---"+e.message)
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
                is SocketException -> {
                    error = "访问域名异常"
                    val isBase = MMKVUtil.getBoolean(Constants.SERVE_BASE_URL)
                    MMKVUtil.save(Constants.SERVE_BASE_URL,!isBase)
                }
                is JsonParseException -> {
                    error = "数据解析错误"
                }
                is UnknownHostException -> {
                    error = "网络未打开,无法解析域名"
                }
                is HttpException -> {
                    when (e.code()) {
                        400 -> {
                            error = "访问域名异常"
                            val isBase = MMKVUtil.getBoolean(Constants.SERVE_BASE_URL)
                            MMKVUtil.save(Constants.SERVE_BASE_URL,!isBase)
                        }
                        401 -> {
                            error = "权限验证错误"
                            EventBus.getDefault().post(LoginEvent())
                        }
                        403 -> error = "访问被禁止"
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
}
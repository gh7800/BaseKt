package cn.shineiot.basic.utils

import android.content.Context
import android.net.ConnectivityManager
import cn.shineiot.basic.BaseApplication
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author GF63
 */
object NetworkUtil {
    private const val TIMEOUT = 3000 // TIMEOUT

    /**
     * 网络是否连接
     */
    fun isNetworkConnected(): Boolean {
        val mConnectivityManager = BaseApplication.context()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager.activeNetworkInfo
        if (mNetworkInfo != null) {
            return mNetworkInfo.isConnected
        }
        return false
    }

    /**
     * pingUrl 是否能通
     */
    fun pingNetWork(url : String = "https://www.baidu.com"): Boolean {
        var result = false
        var httpUrl: HttpURLConnection? = null
        try {
            httpUrl = URL(url).openConnection() as HttpURLConnection
            httpUrl.connectTimeout = TIMEOUT
            httpUrl.connect()
            result = httpUrl.responseCode != 400
        } catch (e: IOException) {

        } finally {
            if (null !== httpUrl) {
                httpUrl.disconnect()
            }
        }
        return result
    }

    fun isWifiConnected(): Boolean {

        val mConnectivityManager = BaseApplication.context()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWiFiNetworkInfo = mConnectivityManager
            .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (mWiFiNetworkInfo != null) {
            return mWiFiNetworkInfo.isAvailable
        }

        return false
    }

    fun isMobileConnected(): Boolean {

        val mConnectivityManager = BaseApplication.context()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mMobileNetworkInfo = mConnectivityManager
            .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable
        }
        return false
    }

    fun getConnectedType(): Int {

            val mConnectivityManager = BaseApplication.context()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null && mNetworkInfo.isAvailable) {
                return mNetworkInfo.type
            }

        return -1
    }

}
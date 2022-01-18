package cn.shineiot.basic.utils

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.Point
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import androidx.core.content.FileProvider
import cn.shineiot.basic.BaseApplication
import java.io.File
import java.util.*


object ScreenUtil {

    /**
     * 获取设备号
     */
    @SuppressLint("HardwareIds")
    fun getImei(context: Context): String? {
        /*val telephonyManager: TelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var deviceId = telephonyManager.deviceId

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID);
        }
        return deviceId*/
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     * 获取App信息
     */
    fun getAppInfo(context: Context): PackageInfo {
        return context.packageManager.getPackageInfo(context.packageName, 0)
    }

    /**
     * 打电话
     */
    fun callPhone(context: Context, phone: String) {
        val intent = Intent(Intent.ACTION_CALL)
        val data = Uri.parse("tel:${phone}")
        intent.data = data
        context.startActivity(intent)
    }

    /**
     * dp转px
     */
    fun dp2px(context: Context, dp: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    /**
     * dp转px
     */
    fun px2dp(context: Context, px: Int): Float {
        val scale: Float = context.resources.displayMetrics.density
        return px / scale + 0.5f
    }

    /**
     * 获取屏幕密度
     */
    fun getDensity(): Float {
        val dm = BaseApplication.context().resources.displayMetrics
        return dm.density
    }

    /**
     * 屏幕宽度
     */
    fun getScreenWidth(): Int {
        val dm = BaseApplication.context().resources.displayMetrics
        return dm.widthPixels
    }

    /**
     * 屏幕高度
     */
    fun getScreenHeight(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val point = Point()
        windowManager.defaultDisplay.getRealSize(point)
//        return displayMetrics.heightPixels

        return point.y
    }



    /**
     * 跳转到应用详情页面
     */
    fun gotoAppDetailIntent(activity: Activity) {
        val intent = Intent()
        intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", activity.packageName, null)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }

    /**
     * 安装APK
     */
    fun installApk(context: Context, file: File) {

        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(
                FileProvider.getUriForFile(
                    context, context.packageName + ".fileProvider", file
                ), "application/vnd.android.package-archive"
            )
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


    /**
     * GPS是否打开
     */
    fun isGpsOPen(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * 设置GPS
     */
    fun gotoSetGPS(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            intent.action = Settings.ACTION_SETTINGS
            context.startActivity(intent)
        }
    }

    /**
     * 获取手机厂商
     * @return  手机厂商
     */
    fun getDeviceBrand(): String {
        return Build.BRAND.toUpperCase(Locale.ROOT)
    }

    /**
     * 获取StatusBar的高度
     */
    fun getStatusBarHeight(activity: Context): Int {
        val resourceId: Int =
            activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        return when (resourceId > 0) {
            true -> {
                val height = activity.resources.getDimensionPixelSize(resourceId)
                //LogUtil.e(height)
                height
            }
            else -> {
                (24 * getDensity()).toInt()
            }
        }
    }

    /**
     * 获取ToolBar的高度
     */
    fun getToolBarHeight(context: Context): Int {
        val tv = TypedValue()
        var actionBarHeight = 0
        if (context.theme.resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(
                tv.data, context.resources.displayMetrics
            )
        }
        //LogUtil.e("$actionBarHeight ${getDensity()}")
        return actionBarHeight

        //return (getDensity() * 56).toInt()
    }

    /**
     * 全面屏（是否开启全面屏开关 0 关闭  1 开启）
     *
     * @param context
     * @return
     */
    fun navigationGestureEnabled(context: Context): Boolean {
        val enable = Settings.Global.getInt(context.contentResolver, getDeviceInfo(), 0)
        return enable != 0
    }

    /**
     * 获取设备信息（目前支持几大主流的全面屏手机，亲测华为、小米、oppo、魅族、vivo都可以）
     *
     * @return
     */
    private fun getDeviceInfo(): String {
        val brand = Build.BRAND
        if (TextUtils.isEmpty(brand)) return "navigationbar_is_min"
        return when {
            brand.equals("HUAWEI", ignoreCase = true) -> {
                "navigationbar_is_min"
            }
            brand.equals("XIAOMI", ignoreCase = true) -> {
                "force_fsg_nav_bar"
            }
            brand.equals("VIVO", ignoreCase = true) -> {
                "navigation_gesture_on"
            }
            brand.equals("OPPO", ignoreCase = true) -> {
                "navigation_gesture_on"
            }
            else -> {
                "navigationbar_is_min"
            }
        }
    }

    /**
     * 非全面屏下 虚拟键实际高度(隐藏后高度为0)
     * @param activity
     * @return
     */
    fun getCurrentNavigationBarHeight(activity: Activity): Int {
        return if (isNavigationBarShown(activity)) {
            getNavigationBarHeight(activity)
        } else {
            0
        }
    }

    /**
     * 非全面屏下 虚拟按键是否打开
     * @param activity
     * @return
     */
    private fun isNavigationBarShown(activity: Activity): Boolean {
        //虚拟键的view,为空或者不可见时是隐藏状态
        val view: View = activity.findViewById(R.id.navigationBarBackground) ?: return false
        val visible: Int = view.visibility
        return !(visible == View.GONE || visible == View.INVISIBLE)
    }

    /**
     * 非全面屏下 虚拟键高度(无论是否隐藏)
     * @param context
     * @return
     */
    private fun getNavigationBarHeight(context: Context): Int {
        var result = 0
        val resourceId =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}
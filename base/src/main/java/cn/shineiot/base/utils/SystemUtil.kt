package cn.shineiot.base.utils

import android.content.Context
import android.net.Uri
import android.os.Bundle

object SystemUtil {


    fun setBadgeNum(context: Context, count: Int) {
        when (ScreenUtil.getDeviceBrand()) {
            PHONE_XIAOMI -> {
            }
            PHONE_REDMI -> {
            }
            PHONE_HUAWEI -> {
                setBadgeNumHuawei(context, count)
            }
            PHONE_HONOR -> {
                setBadgeNumHuawei(context, count)
            }
        }
    }

    /**
     * set 角标 number
     */
    private fun setBadgeNumHuawei(context: Context, num: Int) {
        try {
            val bundle = Bundle()
            bundle.putString("package",context.packageName)        // com.test.badge is your package name
            bundle.putString("class",context.packageManager.getLaunchIntentForPackage(context.packageName)?.component?.className) // com.test. badge.MainActivity is your apk main activity
            bundle.putInt("badgenumber", num)

            context.contentResolver.call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"),
                "change_badge",
                null,
                bundle
            )
        } catch (e: Exception) {
            LogUtil.e("huawei------------- ${e.message}")
        }
    }




    /**
     * 手机品牌
     */
    // 小米
    const val PHONE_XIAOMI = "XIAOMI"

    //红米
    const val PHONE_REDMI = "REDMI"

    // 华为
    const val PHONE_HUAWEI = "HUAWEI"

    // 荣耀
    const val PHONE_HONOR = "HONOR"

    // 魅族
    const val PHONE_MEIZU = "MEIZU"

    // 索尼
    const val PHONE_SONY = "SONY"

    // 三星
    const val PHONE_SAMSUNG = "SAMSUNG"

    // LG
    const val PHONE_LG = "LG"

    // HTC
    const val PHONE_HTC = "HTC"

    // NOVA
    const val PHONE_NOVA = "NOVA"

    // OPPO
    const val PHONE_OPPO = "OPPO"

    // vivo
    const val PHONE_VIVO = "VIVO"

    // 乐视
    const val PHONE_LeMobile = "LEMOBILE"

    // 联想
    const val PHONE_LENOVO = "LENOVO"
}
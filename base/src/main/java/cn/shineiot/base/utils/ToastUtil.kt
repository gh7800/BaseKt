package cn.shineiot.base.utils

import android.annotation.SuppressLint
import android.view.Gravity
import android.widget.Toast
import cn.shineiot.base.BaseApplication

/**
 * @author gf63
 */
class ToastUtil {

    companion object {
        /**
         * 默认居下显示
         */
        @SuppressLint("ShowToast")
        fun setMessage(msg: String?, duration: Int = Toast.LENGTH_SHORT): Toast? {
            if (msg?.isEmpty() != true) {
                var toast: Toast? = null
                if (toast == null) {
                    toast = Toast.makeText(BaseApplication.context(), null, duration)
                }
                toast?.setText(msg)
                return toast
            }
            return null
        }
    }

}
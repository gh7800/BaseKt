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
        var toast: Toast? = null

        @SuppressLint("ShowToast")
        fun show(msg: String?, duration: Int = Toast.LENGTH_SHORT) {
            if (msg?.isEmpty() == true) return
            if (toast == null) {
                toast = Toast.makeText(BaseApplication.context(), null, duration)
            }
            toast?.setText(msg)
            toast?.show()
        }


        /**
         * 默认居下显示
         */
        fun showToast(msg: String?, duration: Int = Toast.LENGTH_SHORT) {
            if (msg?.isEmpty() == true) return
            show(msg, duration)
        }

        /**
         * 取消toast
         */
        fun cancel(){
            toast?.cancel()
            toast = null
        }
    }

}
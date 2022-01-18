package cn.shineiot.basic.utils

import android.annotation.SuppressLint
import android.widget.Toast
import cn.shineiot.basic.BaseApplication

/**
 * 推荐使用 SnackBarUtil，SnackBar回随着activity的销毁而消失
 * 如果当前activity已销毁还需要提示，才用Toast
 * @author gf63
 */
class ToastUtil {

    companion object {
        /**
         * 默认居下显示
         */
        @SuppressLint("ShowToast")
        fun show(msg: String?, duration: Int = Toast.LENGTH_SHORT) {
            Toast.makeText(BaseApplication.context(), msg, duration).show()
        }
    }

}
package cn.shineiot.base.utils

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

/**
 * @Description 小吃店
 * @Author : GF63
 * @Date : 2021/10/19 16:39
 */
class SnackBarUtil {

    //private var snackBar: Snackbar? = null

    /*companion object {
        val instance: SnackBarUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SnackBarUtil()
        }
    }*/

    /**
     * 正常底部显示
     * @param view parentLayout 当前布局的外层容器
     * @param duration SnackBar.LENGTH_INDEFINITE  为一直显示的
     */
    companion object {
        var snackBar: Snackbar? = null

        fun show(
            view: View,
            msg: String?,
            actionMsg: String ?= null,
            listener: View.OnClickListener ?= null,
            duration: Int = Snackbar.LENGTH_SHORT
        ) {
            msg?.let {
                val snackBar = Snackbar.make(view, it, duration)
                if (listener != null) {
                    snackBar.setAction(actionMsg,listener)
                }else if(actionMsg?.isNotEmpty() == true){
                    snackBar.setAction(actionMsg) { cancel() }
                }
                snackBar.show()
            }
        }

        /**
         * 顶部显示
         */
        @SuppressLint("ShowToast")
        fun showTop(
            parent: View,
            msg: String?,
            actionMsg: String = "确定",
            listener: View.OnClickListener? = null,
            duration: Int = Snackbar.LENGTH_SHORT
        ) {
            msg?.let {

                snackBar = Snackbar.make(parent, it, duration)
                val view = snackBar?.view
                val params = view?.layoutParams as CoordinatorLayout.LayoutParams
                params.gravity = Gravity.TOP
                view.layoutParams = params
                if (listener != null) {
                    snackBar?.setAction(actionMsg, listener)
                }
                snackBar?.show()
            }
        }

        /**
         * 取消
         */
        fun cancel() {
            snackBar?.dismiss()
        }
    }


}


package cn.shineiot.base.utils

import android.annotation.SuppressLint
import android.view.Gravity
import android.widget.Toast
import cn.shineiot.base.BaseApplication

/**
 * @author gf63
 */
object ToastUtil {
    private val toast: Toast by lazy { Toast.makeText(BaseApplication.context(),"",Toast.LENGTH_SHORT) }

    /**
     * 默认居下显示
     */
    @SuppressLint("ShowToast")
    fun show(msg: String?, duration : Int = Toast.LENGTH_SHORT) {
        if(null == msg){
            return
        }

        toast.setText(msg)
        toast.duration = duration
        toast.setGravity(Gravity.BOTTOM, 0, 200)
        toast.show()
    }

    /**
     * 默认居下显示
     */
    @SuppressLint("ShowToast")
    fun showToast(msg: String?, duration : Int = Toast.LENGTH_SHORT) {
        if(null == msg){
            return
        }

        toast.setText(msg)
        toast.duration = duration
        toast.setGravity(Gravity.BOTTOM, 0, 200)
        toast.show()
    }

    /**
     * 居中显示
     * @param msg
     */
    @SuppressLint("ShowToast")
    fun showToastCenter(msg: String?, duration : Int = Toast.LENGTH_SHORT) {
        if(null == msg){
            return
        }

        toast.setText(msg)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    /**
     * 居中显示
     * @param msg
     */
    @SuppressLint("ShowToast")
    fun showToastTop(msg: String?, duration : Int = Toast.LENGTH_SHORT) {
        if(null == msg){
            return
        }

        toast.setText(msg)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }


    fun cancelToast() {
        toast.cancel()
    }
}
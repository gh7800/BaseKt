package cn.shineiot.base.utils

import android.text.Editable

/**
 * 给EditText添加text
 */
object EditTextUtil {
    fun getEditable(str:String): Editable? {
        return Editable.Factory.getInstance().newEditable(str)
    }
}
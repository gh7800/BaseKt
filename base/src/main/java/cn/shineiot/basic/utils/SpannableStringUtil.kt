package cn.shineiot.basic.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan

object SpannableStringUtil {
    /**
     * 设置部分字体颜色
     *
     * int flags：取值有如下四个
     * Spannable.SPAN_EXCLUSIVE_EXCLUSIVE：前后都不包括，即在指定范围的前面和后面插入新字符都不会应用新样式
     * Spannable.SPAN_EXCLUSIVE_INCLUSIVE：前面不包括，后面包括。即仅在范围字符的后面插入新字符时会应用新样式
     * Spannable.SPAN_INCLUSIVE_EXCLUSIVE：前面包括，后面不包括。
     * Spannable.SPAN_INCLUSIVE_INCLUSIVE：前后都包括
     */
    @SuppressLint("ResourceAsColor")
    fun setTextColor(
        text: String?,
        color: Int = Color.RED,
        startIndex: Int = 0,
        endIndex: Int = 1,
        flags: Int = SpannableString.SPAN_INCLUSIVE_INCLUSIVE
    ): SpannableString {
        val spannableString = SpannableString(text)
        val foregroundColorSpan = ForegroundColorSpan(color)
        spannableString.setSpan(foregroundColorSpan, startIndex, endIndex, flags)
        return spannableString
    }
}
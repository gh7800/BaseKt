package cn.shineiot.base.widget

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import cn.shineiot.base.BaseApplication
import cn.shineiot.base.R

/**
 * title+content+右侧icon >
 * 带分割线
 */
class LayoutTitleContentIconCommon @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var tvContent: TextView
    private var tvTitle: TextView

    init {
        val layout: View =
            LayoutInflater.from(context).inflate(R.layout.layout_title_content_icon, this)
        tvTitle = layout.findViewById(R.id.layoutTitle)
        tvContent = layout.findViewById(R.id.layoutContent)
        layout.findViewById<View>(R.id.line).visibility = VISIBLE
    }


    /**
     * @param
     * content重心：gravity默认居右
     *
     * 设置字体前景色
     *  msp.setSpan(new ForegroundColorSpan(Color.MAGENTA), 12, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
     */
    @SuppressLint("ResourceAsColor")
    fun setTitleContent(
        title: String?,
        content: String?,
        gravity: Int = Gravity.RIGHT,
        contentColor: Int = 0,
        titleColor: Int = 0
    ) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.text = title
        }
        if (!TextUtils.isEmpty(content)) {
            tvContent.text = content
            tvContent.gravity = gravity
        }

        if (contentColor != 0) {
            tvContent.setTextColor(ContextCompat.getColor(BaseApplication.context(), contentColor))
        }
        if (titleColor != 0) {
            tvTitle.setTextColor(ContextCompat.getColor(BaseApplication.context(), titleColor))
        }

    }

    fun setTitle(title: String?) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.text = title
        }
    }

    fun setContent(content: String?) {
        tvContent.text = content
    }

    fun getContent(): String {
        return tvContent.text.toString()
    }
}
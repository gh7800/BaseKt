package cn.shineiot.base.widget

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import cn.shineiot.base.BaseApplication
import cn.shineiot.base.R
import cn.shineiot.base.utils.LogUtil

/**
 * title+content
 */
class LayoutTitleContentCommon @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var tvContent: TextView
    private var tvTitle: TextView

    init {
        val layout: View = LayoutInflater.from(context).inflate(R.layout.layout_title_content, this)
        tvTitle = layout.findViewById(R.id.layoutTitle)
        tvContent = layout.findViewById(R.id.layoutContent)
        layout.findViewById<View>(R.id.line).visibility = VISIBLE
    }


    /**
     * @param
     * content重心：gravity默认居右
     */
    @SuppressLint("ResourceAsColor", "ResourceType")
    fun setTitleContent(title: String?, content: String?,gravity: Int = -9 ,contentColor: Int = 0,titleColor : Int = 0) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.text = title
        }
        if (!TextUtils.isEmpty(content)) {
            tvContent.text = content
        }
        if(gravity != -9){
            tvContent.gravity = gravity
        }

        //LogUtil.e(contentColor)
        if(contentColor != 0) {
            tvContent.setTextColor(ContextCompat.getColor(BaseApplication.context(), contentColor))
        }
        if(titleColor != 0){
            tvTitle.setTextColor(ContextCompat.getColor(BaseApplication.context(), titleColor))
        }

    }

    fun setTitle(title: String?){
        if(!TextUtils.isEmpty(title)){
            tvTitle.text = title
        }
    }

    fun setContent(content : String?){
        if(!TextUtils.isEmpty(content)){
            tvContent.text = content
        }
    }

    fun getContent():String{
        return tvContent.text.toString()
    }
}
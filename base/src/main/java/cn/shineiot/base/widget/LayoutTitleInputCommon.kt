package cn.shineiot.base.widget

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import cn.shineiot.base.R
import cn.shineiot.base.utils.EditTextUtil

/**
 * 左侧title,右侧输入框
 */
class LayoutTitleInputCommon  @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var tvTitle : TextView
    private var editText: EditText

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_title_input_common,this)
        tvTitle = view.findViewById(R.id.layout_input_title)
        editText = view.findViewById(R.id.layout_input_content)
    }

    fun setTitleInput(title : String = "",hintText: String = "",inputType : Int = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,text:String = "",enable:Boolean = true){
        tvTitle.text = title
        editText.inputType = inputType

        when(hintText.contains("请")){
            true -> {editText.hint = EditTextUtil.getEditable(hintText)}
            else -> {editText.text = EditTextUtil.getEditable(hintText)}
        }
        
        if(text.isNotEmpty()){
            setInputContent(text)
        }

        editText.isEnabled = enable

    }

    fun setInputContent(content : String){
        editText.text = EditTextUtil.getEditable(content)
    }

    fun getInput() : String{
        return when(editText.text){
            null -> ""
            else -> editText.text.toString()
        }
    }
}
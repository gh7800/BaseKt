package cn.shineiot.base.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText

/**
 * 自定义EditText
 */
class IEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    private var rightDrawable: Drawable? = null

    init {

        rightDrawable = compoundDrawables[2]

        setHideRightClearDrawable(false)
        addTextChangedListener(TextWatchImp())
    }

    fun setHideRightClearDrawable(visible: Boolean) {
        if (visible) {
            setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                rightDrawable,
                compoundDrawables[3]
            )
        } else {
            setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                null,
                compoundDrawables[3]
            )
        }
    }

    var clearEditTextListener: ClearEditTextListener? = null

    fun setEditTextListener(clearEditTextListener: ClearEditTextListener) {
        this.clearEditTextListener = clearEditTextListener
    }

    interface ClearEditTextListener {
        fun clearEditText()
    }


    private inner class TextWatchImp : TextWatcher {
        override fun afterTextChanged(text: Editable?) {
            /*判断输入框有没有内容，设置是否显示删除按钮*/
            if ("" != text.toString().trim { it <= ' ' } && text.toString().trim { it <= ' ' }
                    .isNotEmpty()) {
                setHideRightClearDrawable(true)
            } else {
                setHideRightClearDrawable(false)
                clearEditTextListener?.clearEditText()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (compoundDrawables[2] != null) {
            if (event?.action == MotionEvent.ACTION_UP) {
                val touchable = event.x > (width - paddingRight - (rightDrawable?.intrinsicWidth
                    ?: 0)) && (event.x < (width - paddingRight))

                if (touchable) {

                    val type = this.inputType
                    if (type == InputType.TYPE_TEXT_VARIATION_PASSWORD + 1 || type == InputType.TYPE_NUMBER_VARIATION_PASSWORD + 2) { //密码显示和隐藏
                        transformationMethod =
                            if (transformationMethod == PasswordTransformationMethod.getInstance()) {
                                HideReturnsTransformationMethod.getInstance()
                            } else {
                                PasswordTransformationMethod.getInstance()
                            }
                    } else {  //非密码，清空
                        this.setText("")
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }
}
package cn.shineiot.base.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import cn.shineiot.base.R

class PasswordEditText(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    private var hideDrawable: Drawable? = null
    private var showDrawable: Drawable? = null
    private var isVisible = false

    init {
        hideDrawable = compoundDrawables[2]
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText)

        if (null == hideDrawable) {
            hideDrawable = typeArray.getDrawable(R.styleable.PasswordEditText_hideDrawable)
            showDrawable = typeArray.getDrawable(R.styleable.PasswordEditText_showDrawable)

            hideDrawable?.setBounds(
                0,
                0,
                hideDrawable!!.intrinsicWidth,
                hideDrawable!!.intrinsicHeight
            )
            showDrawable?.setBounds(
                0,
                0,
                showDrawable!!.intrinsicWidth,
                showDrawable!!.intrinsicHeight
            )
        }
        typeArray.recycle()

        addTextChangedListener(TextWatchImp())
        setRightDrawable(false)
    }


    private fun setRightDrawable(visible: Boolean) {
        val drawable: Drawable? =
            if (visible) {
                hideDrawable
            } else {
                null
            }

        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1],
            drawable,
            compoundDrawables[3]
        )
    }

    private fun isOpen(show: Boolean) {
        val drawable: Drawable? =
            if (show) {
                showDrawable
            } else {
                hideDrawable
            }
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1],
            drawable,
            compoundDrawables[3]
        )
    }

    private inner class TextWatchImp : TextWatcher {
        override fun afterTextChanged(text: Editable?) {
            /*判断输入框有没有内容*/
            if ("" != text.toString().trim { it <= ' ' } && text.toString().trim { it <= ' ' }
                    .isNotEmpty()) {
                setRightDrawable(true)
            } else {
                setRightDrawable(false)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (hideDrawable != null) {
            if (event?.action == MotionEvent.ACTION_UP) {
                val touchable =
                    event.x > (width - paddingRight - (hideDrawable?.intrinsicWidth
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
                        isVisible = !isVisible
                        isOpen(isVisible)
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }
}
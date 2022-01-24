package cn.shineiot.base.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
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
import cn.shineiot.base.R
import cn.shineiot.base.utils.LogUtil

/**
 * 自定义EditText，边框颜色、边框宽度、边框圆角半径
 *
 * 设置drawableRight,密码输入类型可以查看密码\非密码类型为清空输入框
 *
 * 默认背景白色
 */
@SuppressLint("Recycle", "ResourceAsColor")
class IEditText constructor(
    context: Context,
    attrs: AttributeSet,
) : AppCompatEditText(context, attrs) {

    private var rightDrawable: Drawable? = null
    private var radius: Int = 0
    private var borderColor: Int = R.color.white
    private var borderWidth: Int = 1

    //自定背景边框Drawable
    private var gradientDrawable: GradientDrawable? = null

    private var builder: CustomBuilder? = null

    init {

        rightDrawable = compoundDrawables[2]

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IEditText)
        radius = typedArray.getDimension(R.styleable.IEditText_et_radius, 0F).toInt()
        borderWidth = typedArray.getDimension(R.styleable.IEditText_et_borderWidth, 0F).toInt()
        borderColor = typedArray.getColor(R.styleable.IEditText_et_borderColor, 0)
        typedArray.recycle()

        if (radius > 0) {
            setStyle()
        }

        setHideRightClearDrawable(false)
        addTextChangedListener(TextWatchImp())
    }

    /**
     * 自定义样式
     */
    private fun setStyle() {
        setBuilder(
            CustomBuilder()
                .setShape(GradientDrawable.RECTANGLE)
                .setGradientType(GradientDrawable.RADIAL_GRADIENT)
                .setStrokeWidth(borderWidth)  //边框宽度
                .setStrokeColor(borderColor)    //边框颜色
                .setTopLeftRadius(radius)   //左上角边框半径
                .setTopRightRadius(radius)   //右上角边框半径
                .setBottomLeftRadius(radius)   //左下角边框半径
                .setBottomRightRadius(radius)   //右下角边框半径
                .setSolidColor(Color.WHITE)
        )
    }

    private fun setBuilder(builder: CustomBuilder) {
        this.builder = builder
        setCustomBackground()
    }

    fun getBuilder(): CustomBuilder? {
        return builder
    }

    fun getGradientDrawable(): GradientDrawable? {
        return gradientDrawable
    }

    /**设置editText的backGround*/
    private fun setCustomBackground() {
        //默认背景
        gradientDrawable = GradientDrawableUtil.init().getNormalDrawable(builder)
        this.background = gradientDrawable
        this.isFocusable = true
        this.isFocusableInTouchMode = true
    }

    /**设置显示、隐藏drawableRight*/
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

    /**清除输入框的事件接口*/
    interface ClearEditTextListener {
        fun clearEditText()
    }


    private inner class TextWatchImp : TextWatcher {
        override fun afterTextChanged(text: Editable?) {
            /**判断输入框有没有内容，设置是否显示删除按钮*/
            if ("" != text.toString().trim() && text.toString().trim().isNotEmpty()) {
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

    /**
     * 点击右侧图片的事件
     */
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
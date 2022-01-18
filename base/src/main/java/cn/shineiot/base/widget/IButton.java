package cn.shineiot.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;

import cn.shineiot.base.R;

/**
 * @author gf63
 * 自定义Button
 */
public class IButton extends androidx.appcompat.widget.AppCompatButton {

    private final int normal_color; //正常颜色
    private final int pressed_color;//按下颜色
    private final int enabled_color;//不可用的颜色
    private final int radius;//半径

    public IButton(Context context) {
        this(context,null);
    }

    public IButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.IButton,defStyleAttr,0);
        normal_color = typeArray.getColor(R.styleable.IButton_normal_color,0);
        pressed_color = typeArray.getColor(R.styleable.IButton_pressed_color,0);
        enabled_color = typeArray.getColor(R.styleable.IButton_enabled_color,0);
        radius = (int) typeArray.getDimension(R.styleable.IButton_radius,0.0f);
        typeArray.recycle();

        init();
    }

    private void init() {
        setBackgroundDrawable(getStateListDrawable(getSolidRectDrawable(radius,pressed_color),getSolidRectDrawable(radius,normal_color)));
    }
    /**
     * 得到实心的drawable, 一般作为选中，点中的效果
     *
     * @param cornerRadius 圆角半径
     * @param solidColor   实心颜色
     * @return 得到实心效果
     */
    public static GradientDrawable getSolidRectDrawable(int cornerRadius, int solidColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        // 设置矩形的圆角半径
        gradientDrawable.setCornerRadius(cornerRadius);
        // 设置绘画图片色值
        gradientDrawable.setColor(solidColor);
        // 绘画的是矩形
        gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        return gradientDrawable;
    }

    /**
     * 背景选择器
     *
     * @param pressedDrawable 按下状态的Drawable
     * @param normalDrawable  正常状态的Drawable
     * @return 状态选择器
     */
    public StateListDrawable getStateListDrawable(Drawable pressedDrawable, Drawable normalDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, normalDrawable);
        //设置不能用的状态
        //默认其他状态背景
        GradientDrawable gray = getSolidRectDrawable(radius, enabled_color);
        stateListDrawable.addState(new int[]{}, gray);
        return stateListDrawable;
    }
}

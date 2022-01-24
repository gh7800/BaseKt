package cn.shineiot.base.widget;

import android.graphics.drawable.GradientDrawable;

/**
 * @Description TODO
 * @Author : GF63
 * @Date : 2022/1/24 11:12
 */
public interface BuilderImp {

    BuilderImp setShape(@CustomBuilder.Shape int shape);

    BuilderImp setSolidColor(int solidColor);

    BuilderImp setStrokeColor(int strokeColor);

    BuilderImp setColors(int startColor, int centerColor, int endColor);

    BuilderImp setSelectColors(int startColor, int centerColor, int endColor);

    BuilderImp setOrientation(GradientDrawable.Orientation orientation);

    BuilderImp setGradientType(@CustomBuilder.GradientType int gradientType);

    BuilderImp setGradientRadius(float gradientRadius);

    BuilderImp setStrokeWidth(int strokeWidth);

    BuilderImp setDashWidth(float dashWidth);

    BuilderImp setDashGap(float dashGap);

    BuilderImp setRadius(int radius);

    BuilderImp setTopLeftRadius(int topLeftRadius);

    BuilderImp setTopRightRadius(int topRightRadius);

    BuilderImp setBottomLeftRadius(int bottomLeftRadius);

    BuilderImp setBottomRightRadius(int bottomRightRadius);

    BuilderImp setOpenSelector(boolean openSelector);

    BuilderImp setTextNormalColor(int textNormalColor);

    BuilderImp setTextSelectColor(int textSelectColor);

    BuilderImp setSolidSelectColor(int solidSelectColor);

    BuilderImp setStrokeSelectColor(int strokeSelectColor);
}

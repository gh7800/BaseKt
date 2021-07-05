package cn.shineiot.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingParent;

/**
 * @author GF63
 */
public class ISwipeRefreshLayout extends ViewGroup implements NestedScrollingChild, NestedScrollingParent {


    public ISwipeRefreshLayout(Context context) {
        super(context);
    }

    public ISwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ISwipeRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ISwipeRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


}

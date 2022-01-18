package cn.shineiot.base.listener;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author GF63
 */
public abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    //用来标记是否正在向上滑动
    private boolean isSlidingUpward = false;
    private int lastItemCount = 0;
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        // 当不滑动时
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            //获取最后一个完全显示的itemPosition
            int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();

            int itemCount = manager.getItemCount();

            // 判断是否滑动到了最后一个item，并且是向上滑动
            if (lastItemPosition == (itemCount - 1) && isSlidingUpward && lastItemCount != itemCount) {
                //加载更多
                lastItemCount = itemCount;
                onLoadMore();
            }
        }
    }
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
        isSlidingUpward = dy > 0;
    }
    /**
     * 加载更多回调
     */
    public abstract void onLoadMore();
}


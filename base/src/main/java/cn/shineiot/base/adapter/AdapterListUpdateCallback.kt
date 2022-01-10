package cn.shineiot.base.adapter

import androidx.recyclerview.widget.RecyclerView

import androidx.annotation.NonNull

import androidx.recyclerview.widget.ListUpdateCallback


// 基于 RecyclerView.Adapter 实现的列表更新回调
class AdapterListUpdateCallback(private val mAdapter: RecyclerView.Adapter<*>) :
    ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {
        // 区间插入
        mAdapter.notifyItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        // 区间移除
        mAdapter.notifyItemRangeRemoved(position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        // 移动
        mAdapter.notifyItemMoved(fromPosition, toPosition)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        // 区间更新
        mAdapter.notifyItemRangeChanged(position, count, payload)
    }
}
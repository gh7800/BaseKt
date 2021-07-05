package cn.shineiot.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cn.shineiot.base.R
import cn.shineiot.base.utils.LogUtil
import java.util.*

/**
 * 封装BaseAdapter
 *
 *
 * itemChick 是item和child的点击事件，child的设置方法holder.view.onClick {itemClick.OnItemClick(...)}
 *
 * loadMoreListener 加载更多
 * isLoadEnd 如果实现了加载更多，数据完全加载完的时候需要设置 setLoadEnd(false)
 */
open class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val loadNormalType: Int = 0
    private val loadMoreType: Int = 1
    private val loadEmptyViewType: Int = 2

    private var isEmptyLayout: Boolean = false //是否加载空布局
    private var mEmptyLayout: Int? = null

    private var mList: MutableList<T> = arrayListOf()

    private var mLayoutId: Int? = null
    var itemClick: ItemClick? = null
    var itemLongClick: ItemLongClick? = null

    private var loadMoreListener: OnLoadMoreListener? = null
    private var isLoadEnd: Boolean = false

    constructor()

    constructor(mLayoutId: Int) {
        this.mLayoutId = mLayoutId
    }

    constructor(mList: MutableList<T>?, mLayoutId: Int) {
        this.mLayoutId = mLayoutId
        if (null != mList) {
            this.mList = mList
        }
    }

    fun getData(): MutableList<T> {
        return mList
    }

    /**
     * 第一次添加
     */
    fun setData(list: MutableList<T>) {
        if (mList.size > 0) {
            mList.clear()
            setLoadEnd(false)
            notifyDataSetChanged()
        }
        addData(list)
    }

    fun updateData(position: Int, t: T? = null) {
        notifyItemChanged(position)
        if (null != t) {
            Collections.replaceAll(mList as List<T?>?, t, mList[position])
        }
    }

    fun addDataDiff(list: MutableList<T>) {

        val oldList = getData()

        val callBack = IDiffUtilCallBack(oldList, list)
        val diffResult = DiffUtil.calculateDiff(callBack, true)

        when {
            list.size > 0 -> {
                if (isEmptyLayout) {
                    isEmptyLayout = false
                }
                diffResult.dispatchUpdatesTo(this)
            }
        }
    }

    fun addDataList(index: Int, list: MutableList<T>?) {
        if (!list.isNullOrEmpty()) {
            mList.addAll(index, list)
            notifyDataSetChanged()
        }
    }

    /**
     * 追加
     */
    fun addData(list: MutableList<T>) {
        //addDataDiff(list)

        when {
            list.size > 0 -> {
                if (isEmptyLayout) {
                    isEmptyLayout = false
                }

                val position = mList.size

                if (mList.size == 0) {
                    mList = list
                } else {
                    this.mList.addAll(list)
                }

                notifyItemChanged(position)
            }
            null != mEmptyLayout -> {
                isEmptyLayout = true
                notifyDataSetChanged()
            }
            else -> {

                if (mList.size == 0) {
                    mList = list
                }
                notifyDataSetChanged()
            }
        }
    }

    /**
     * 追加单个元素
     */
    fun addElement(index: Int, t: T) {
        mList.add(index, t)
        notifyDataSetChanged()
    }

    fun addElement(t: T) {
        mList.add(t)
        notifyDataSetChanged()
    }

    /**
     * 在前面追加
     */
    fun addFirstData(list: MutableList<T>?) {
        this.mList.addAll(0, list!!)
        notifyDataSetChanged()
    }

    fun removeData(item: T) {
        this.mList.remove(item)
        notifyDataSetChanged()
    }

    fun removeDataList(list: MutableList<T>?) {
        if (list != null) {
            mList.removeAll(list)
            notifyDataSetChanged()
        }
    }

    fun removeData(position: Int) {
        mList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,mList.size)
    }

    fun removeAllData() {
        this.mList.clear()
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T {
        return mList[position]
    }

    fun getPosition(t: T): Int {
        return mList.indexOf(t)
    }


    override fun getItemCount(): Int {
        return if (null != loadMoreListener && mList.size > 0 && !isLoadEnd) {
            mList.size + 1
        } else if (mList.size == 0 && isEmptyLayout) {
            1
        } else {
            mList.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mList.size && mList.size > 0 && null != loadMoreListener && !isLoadEnd) {
            loadMoreType
        } else if (mList.size == 0 && isEmptyLayout) {
            //LogUtil.e("加载空布局")
            loadEmptyViewType
        } else {
            loadNormalType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //LogUtil.e("${viewType}_viewType")

        return if (viewType == loadNormalType) {
            val v = LayoutInflater.from(parent.context).inflate(this.mLayoutId!!, parent, false)
            KtViewHolder(v)
        } else if (viewType == loadMoreType) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.foot_layout, parent, false)
            FootViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(mEmptyLayout!!, parent, false)
            EmptyLayoutViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is KtViewHolder) {
            if (mList.size <= 0 || position >= mList.size) {
                return
            }
            val item = mList[position]

            // item点击事件，position使用holder.adapterPosition 以防止位置错乱
            holder.itemView.setOnClickListener {
                itemClick?.OnItemClick(holder.itemView, holder.adapterPosition)
            }

            holder.itemView.setOnLongClickListener {
                itemLongClick?.OnItemLongClick(holder.itemView, holder.adapterPosition)
                false
            }

            convert(holder.itemView, item, holder.adapterPosition)

        } else if (holder is FootViewHolder) {
            loadMoreListener!!.onLoadMore()
        }
    }

    open fun convert(itemView: View?, item: T, position: Int) {

    }

    open class KtViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        open fun <T : View?> getView(id: Int): T {
            return itemView.findViewById<T>(id)
        }
    }

    open class FootViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        open fun <T : View?> getView(id: Int): T {
            return itemView.findViewById<T>(id)
        }
    }

    open class EmptyLayoutViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        open fun <T : View?> getView(id: Int): T {
            return itemView.findViewById<T>(id)
        }
    }

    fun setEmptyLayout(mLayoutId: Int) {
        this.mEmptyLayout = mLayoutId
    }

    /**
     * 加载到最后一页,需要设置true
     */
    fun setLoadEnd(loadEnd: Boolean) {
        isLoadEnd = loadEnd
    }

    //设置加载更多
    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.loadMoreListener = onLoadMoreListener
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    fun setItemClickListener(itemClick: ItemClick) {
        this.itemClick = itemClick
    }

    fun setItemLongClickListener(itemLongClick: ItemLongClick) {
        this.itemLongClick = itemLongClick
    }

    interface ItemClick {
        fun OnItemClick(v: View, position: Int)
    }

    interface ItemLongClick {
        fun OnItemLongClick(v: View, position: Int)
    }

}
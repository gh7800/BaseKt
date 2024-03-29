package cn.shineiot.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.shineiot.base.R
import cn.shineiot.base.bean.Pagination
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

/**
 * 封装BaseAdapter 添加数据 setData
 *
 * itemChick 是item和child的点击事件，child的设置方法holder.view.onClick {itemClick.OnItemClick(...)}
 *
 * loadMoreListener 加载更多
 * isLoadEnd 如果实现了加载更多，数据完全加载完的时候需要设置 setLoadEnd(false)
 *
 * isLoadError 是否加载404 layout
 */
abstract class BaseAdapterVB<VB : ViewDataBinding,T> : RecyclerView.Adapter<RecyclerView.ViewHolder>, CoroutineScope {
    //job用于控制协程,后面launch{}启动的协程,返回的job就是这个job对象
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    abstract fun getViewBinding(
        viewType: Int,
        from: LayoutInflater,
        parent: ViewGroup
    ): VB

    abstract fun convert(vBinding: VB, item: T, position: Int)

    open fun getViewType(position: Int): Int {
        return loadNormalType
    }

    private val loadNormalType: Int = 0
    private val loadMoreType: Int = 91
    private val loadEmptyViewType: Int = 92
    private val loadErrorViewType: Int = 93

    private var isEmptyLayout: Boolean = false //是否加载空布局
    private var isErrorLayout: Boolean = false //是否加载错误布局
    private var mEmptyLayout: Int? = null
    private var isLoadError = false //是否显示错误布局

    private var mList: MutableList<T> = arrayListOf()

    private var mLayoutId: Int? = null
    var itemClick: ItemClick? = null
    var itemLongClick: ItemLongClick? = null

    private var loadMoreListener: OnLoadMoreListener? = null
    private var tryRefreshListener: OnTryRefreshListener? = null
    private var isLoadEnd: Boolean = false

    var dispatcher: CoroutineDispatcher = Dispatchers.IO // 默认在IO共享线程池中执行比对

    constructor() {
        job = Job()
    }

    constructor(mLayoutId: Int) {
        this.mLayoutId = mLayoutId
        job = Job()
    }

    constructor(mList: MutableList<T>?, mLayoutId: Int) {
        this.mLayoutId = mLayoutId
        job = Job()
        if (null != mList) {
            this.mList = mList
        }
    }

    fun getData(): MutableList<T> {
        return mList
    }

    //刷新列表
    private fun refreshList(list: MutableList<T>) {
        val size = mList.size
        mList.clear()
        notifyItemRangeRemoved(0, size)
        mList.addAll(list)
        notifyDataSetChanged()
    }

    //更新一条数据
    fun updateData(position: Int, t: T? = null) {
        notifyItemChanged(position)
        if (null != t) {
            Collections.replaceAll(mList as List<T?>?, t, mList[position])
        }
    }

    private val listUpdateCallback: ListUpdateCallback = AdapterListUpdateCallback(this)

    /**
     * DiffUtil
     */
    @Deprecated("-")
    fun addDataDiff(newList: MutableList<T>) {
        when {
            newList.isNotEmpty() -> {
                if (isEmptyLayout) {
                    isEmptyLayout = false
                }
            }
        }
        submitList(newList)
    }

    // 可装填任何类型的新旧列表
    var oldList = arrayListOf<T>()

    //var newList = arrayListOf<T>()
    // 用于标记每一次提交列表
    private var maxSubmitGeneration: Int = 0

    /** 提交新列表*/
    private fun submitList(newList: MutableList<T>) {

        val submitGeneration = ++maxSubmitGeneration
        mList = newList as ArrayList<T>
        // 快速返回：没有需要更新的东西
        if (this.oldList == newList) return
        // 快速返回：旧列表为空，全量接收新列表
        if (this.oldList.isEmpty()) {
            this.oldList = newList
            // 保存列表最新数据的快照
            mList = newList
            listUpdateCallback.onInserted(0, newList.size)
            return
        }

        // 启动协程比对数据
        launch {
            val dataDiffer = IDiffUtilCallBack(oldList, mList)
            val diffResult = DiffUtil.calculateDiff(dataDiffer, true)
            // 保存列表最新数据的快照
            //mList = newList
            oldList = newList
            // 将比对结果抛到主线程并应用到ListUpdateCallback接口
            withContext(Dispatchers.Main) {
                // 只保留最后一次提交的比对结果，其他的都被丢弃
                if (submitGeneration == maxSubmitGeneration) {
                    diffResult.dispatchUpdatesTo(listUpdateCallback)
                }
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        dispatcher.cancel()
    }

    fun addDataList(index: Int, list: MutableList<T>?) {
        if (!list.isNullOrEmpty()) {
            mList.addAll(index, list)
            notifyDataSetChanged()
        }
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

    /**
     * 推荐使用
     * @param pagination 根据页数判断
     */
    fun setData(list: MutableList<T>, pagination: Pagination) {
        if (pagination.current_page == 1) {
            if (mList.size > 0) {
                mList.clear()
                notifyDataSetChanged()
            }
        }
        addData(list,pagination)
    }

    /**
     * 追加
     */
    fun addData(list: MutableList<T>, pagination: Pagination? = null) {
        when {
            list.size > 0 -> {
                if (isEmptyLayout) {
                    isEmptyLayout = false
                }

                val position = mList.size
                this.mList.addAll(list)

                if (position == 0) {
                    notifyDataSetChanged()
                } else {
                    notifyItemChanged(position)
                }
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

        pagination?.let {
            setLoadEnd(it.current_page >= it.last_page)
        }
    }

    /**
     * 追加单个元素
     */
    fun addElement(index: Int, t: T) {
        mList.add(index, t)
        notifyItemInserted(index)
        notifyItemChanged(mList.size)
    }

    fun addElement(t: T) {
        mList.add(t)
        notifyItemInserted(mList.size - 1)
        notifyItemChanged(itemCount)
    }

    /**
     * 在 index 位置追加
     */
    fun addData(list: MutableList<T>?, index: Int = 0) {
        this.mList.addAll(index, list!!)
        notifyDataSetChanged()
    }

    fun removeData(item: T) {
        val index = mList.indexOf(item)
        removeData(index)
    }

    fun removeDataList(list: MutableList<T>?) {
        list?.map {
            removeData(it)
        }
    }

    fun removeData(position: Int) {
        mList.removeAt(position)
        if (mList.isEmpty()) {
            if (mEmptyLayout != null) {
                isEmptyLayout = true
            }
        }
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mList.size)
    }

    fun removeAllData() {
        this.mList.clear()
        if (mEmptyLayout != null) {
            isEmptyLayout = true
        }
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
        } else if (mList.isEmpty() && isLoadError) {
            1
        } else if (mList.isEmpty() && isEmptyLayout) {
            1
        } else {
            mList.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mList.size && mList.size > 0 && null != loadMoreListener && !isLoadEnd) {
            loadMoreType
        } else if (mList.isEmpty() && isLoadError) {
            loadErrorViewType
        } else if (mList.isEmpty() && isEmptyLayout) {
            loadEmptyViewType
        } else {
            getViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //LogUtil.e("${viewType}_viewType")

        return when (viewType) {
            loadMoreType -> {
                val v =
                    LayoutInflater.from(parent.context).inflate(R.layout.foot_layout, parent, false)

                val holder = FootViewHolder(v)
                val lp = holder.itemView.layoutParams
                if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                    lp.isFullSpan = true
                }
                holder
            }
            loadErrorViewType -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.error_layout, parent, false)
                val holder = ErrorLayoutViewHolder(v)
                val lp = holder.itemView.layoutParams
                if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                    lp.isFullSpan = true
                }
                holder
            }
            loadEmptyViewType -> {
                val v = LayoutInflater.from(parent.context).inflate(mEmptyLayout!!, parent, false)
                val holder = EmptyLayoutViewHolder(v)
                val lp = holder.itemView.layoutParams
                if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                    lp.isFullSpan = true
                }
                holder
            }
            else -> {
                //val v = LayoutInflater.from(parent.context).inflate(this.mLayoutId!!, parent, false)
                KtViewHolder(getViewBinding(viewType, LayoutInflater.from(parent.context), parent))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is KtViewHolder -> {
                if (mList.size <= 0 || position >= mList.size) {
                    return
                }
                val item = mList[position]

                // item点击事件，position使用holder.adapterPosition 以防止位置错乱
                holder.itemView.setOnClickListener {
                    itemClick?.onItemClick(holder.itemView, holder.bindingAdapterPosition)
                }

                holder.itemView.setOnLongClickListener {
                    itemLongClick?.OnItemLongClick(holder.itemView, holder.bindingAdapterPosition)
                    false
                }

                convert(holder.viewDataBinding as VB, item, holder.bindingAdapterPosition)

            }
            is FootViewHolder -> {
                loadMoreListener?.onLoadMore()
            }
            is ErrorLayoutViewHolder -> {
                holder.getView<AppCompatTextView>(R.id.elBt).setOnClickListener {
                    isLoadError = false
                    notifyDataSetChanged()

                    tryRefreshListener?.onTryRefresh()
                }
            }
        }
    }


    open class KtViewHolder(viewDataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        val viewDataBinding = viewDataBinding
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

    open class ErrorLayoutViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        open fun <T : View?> getView(id: Int): T {
            return itemView.findViewById<T>(id)
        }
    }

    //设置加载错误时的layout
    fun setErrorStatus() {
        isLoadError = true
        notifyDataSetChanged()
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

    fun setOnTryRefreshListener(onTryRefreshListener: OnTryRefreshListener) {
        this.tryRefreshListener = onTryRefreshListener
    }

    //设置加载更多
    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.loadMoreListener = onLoadMoreListener
    }

    fun setItemClickListener(itemClick: ItemClick) {
        this.itemClick = itemClick
    }

    fun setItemLongClickListener(itemLongClick: ItemLongClick) {
        this.itemLongClick = itemLongClick
    }

    interface OnTryRefreshListener {
        fun onTryRefresh()
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    interface ItemClick {
        fun onItemClick(v: View, position: Int)
    }

    interface ItemLongClick {
        fun OnItemLongClick(v: View, position: Int)
    }

}

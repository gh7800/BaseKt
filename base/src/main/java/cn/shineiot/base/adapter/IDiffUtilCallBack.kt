package cn.shineiot.base.adapter

import androidx.recyclerview.widget.DiffUtil
import cn.shineiot.base.utils.LogUtil

class IDiffUtilCallBack<T>(private var list: MutableList<T>, private var newList: MutableList<T>) : DiffUtil.Callback(){

    override fun getOldListSize(): Int {
        return list.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    //比较是否是同一个item
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        val oldItem = list[oldItemPosition] as? IDiff
        val newItem = newList[newItemPosition] as? IDiff

        if (oldItem == null || newItem == null){
            LogUtil.e("areItemsTheSame_1")
            return false
        }
        LogUtil.e("areItemsTheSame_2__${oldItem.equals(newItem)}}")
        return oldItem == newItem
    }

    //item相同，内容是否相同
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        val oldItem = list[oldItemPosition]
        val newItem = newList[newItemPosition]

        LogUtil.e("areContentsTheSame___${oldItem == newItem}}")

        return oldItem.hashCode() == newItem.hashCode()
    }

    //返回改变的数据
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {

        val oldItem = list[oldItemPosition] as? IDiff
        val newItem = newList[newItemPosition] as? IDiff
        if (oldItem == null || newItem == null) {
            LogUtil.e("getChangePayload_1")
            return null
        }
        LogUtil.e("getChangePayload_2_${oldItem diff newItem}")
        return oldItem diff newItem // 中缀表达式
    }
}
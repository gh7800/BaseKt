package cn.shineiot.basic.homeFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import cn.shineiot.base.adapter.BaseAdapter
import cn.shineiot.base.adapter.BaseAdapterVD
import cn.shineiot.basic.databinding.ItemHomeBinding

class HomeAdapter : BaseAdapterVD<String>() {

    override fun getViewBinding(
        viewType: Int,
        from: LayoutInflater,
        parent: ViewGroup
    ): ViewDataBinding {
        return ItemHomeBinding.inflate(from,parent,false)
    }

    override fun getViewType(position: Int): Int {
        return super.getViewType(position)
    }

    override fun convert(itemView: ViewDataBinding?, item: String, position: Int) {
        val viewBinding = itemView as ItemHomeBinding
        viewBinding.apply {
            title.text = item
            title.setOnClickListener {
                itemClick?.OnItemClick(it,position)
            }
        }
    }

}
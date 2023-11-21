package cn.shineiot.basic.homeFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import cn.shineiot.base.adapter.BaseAdapterVB
import cn.shineiot.basic.databinding.ItemHomeBinding

class HomeAdapter : BaseAdapterVB<ItemHomeBinding,String>() {

    override fun getViewBinding(
        viewType: Int,
        from: LayoutInflater,
        parent: ViewGroup
    ): ItemHomeBinding {
        return ItemHomeBinding.inflate(from,parent,false)
    }

    override fun convert(vBinding: ItemHomeBinding, item: String, position: Int) {
        vBinding.apply {
            title.text = item
            title.setOnClickListener {
                itemClick?.onItemClick(it,position)
            }
        }
    }

    override fun getViewType(position: Int): Int {
        return super.getViewType(position)
    }

    /*override fun convert(itemView: ViewDataBinding?, item: String, position: Int) {
        val viewBinding = itemView as ItemHomeBinding
        viewBinding.apply {
            title.text = item
            title.setOnClickListener {
                itemClick?.OnItemClick(it,position)
            }
        }
    }*/

}
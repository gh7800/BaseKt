package cn.shineiot.basic.homeFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import cn.shineiot.base.adapter.BaseAdapter
import cn.shineiot.basic.databinding.ItemHomeBinding

class HomeAdapter : BaseAdapter<String>() {

    override fun getViewBinding(
        viewType: Int,
        from: LayoutInflater,
        parent: ViewGroup
    ): ViewBinding {
        return ItemHomeBinding.inflate(from,parent,false)
    }

    override fun getViewType(position: Int): Int {
        return 0
    }

    override fun convert(vb: ViewBinding, item: String, position: Int) {

        val viewBinding = vb as ItemHomeBinding
        viewBinding.apply {
            title.text = item
            title.setOnClickListener {
                itemClick?.OnItemClick(it,position)
            }
        }

    }

}
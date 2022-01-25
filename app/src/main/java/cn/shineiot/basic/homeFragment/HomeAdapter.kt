package cn.shineiot.basic.homeFragment

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import cn.shineiot.base.adapter.BaseAdapter
import cn.shineiot.basic.R

class HomeAdapter(layout: Int) : BaseAdapter<String>(layout) {
    override fun convert(itemView: View, item: String, position: Int) {
        super.convert(itemView, item, position)

        //优化viewBinding todo
        itemView.findViewById<AppCompatTextView>(R.id.title).text = item

//        itemView.title.text = item
    }
}
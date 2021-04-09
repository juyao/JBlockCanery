package com.jandroidlibrary.jblockcannary

import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jandroidlibrary.jblockcannary.dao.BlockBean

/**
日期 :4/8/21
作者: juyao
描述：
版本：
 */
class BlockAdapter : RecyclerView.Adapter<MyViewHolder>() {
    var dataList: List<BlockBean> = ArrayList()

    var clickListener: BlockItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.listitem_block, parent, false)
        return MyViewHolder(itemView)
        Looper.loop()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bean = dataList[position]
        holder?.apply {
            tv_method.text = bean.methodName
            tv_blockTime.text = "卡顿耗时${bean.blockTime}ms"
            tv_date.text = bean.date
            itemView.setOnClickListener {
                clickListener?.click(bean)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}

interface BlockItemClickListener {
    fun click(item: BlockBean)
}


class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tv_method = itemView.findViewById<TextView>(R.id.tv_methodName)
    val tv_blockTime = itemView.findViewById<TextView>(R.id.tv_blocktime)
    val tv_date = itemView.findViewById<TextView>(R.id.tv_blockdate)
}
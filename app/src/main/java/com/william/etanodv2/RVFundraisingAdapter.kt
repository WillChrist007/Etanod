package com.william.etanodv2

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.william.etanodv2.room.items.Item
import kotlinx.android.synthetic.main.adapter_item.view.*

class RVFundraisingAdapter (private val items: ArrayList<Item>, private val
listener: OnAdapterListener
) :
    RecyclerView.Adapter<RVFundraisingAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ItemViewHolder {
        return ItemViewHolder(

            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_item,parent, false)
        )
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position:
    Int) {
        val item = items[position]
        holder.view.text_title.text = item.judul
        holder.view.text_title.setOnClickListener{
            listener.onClick(item)
        }
        holder.view.icon_edit.setOnClickListener {
            listener.onUpdate(item)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onDelete(item)
        }
    }
    override fun getItemCount() = items.size
    inner class ItemViewHolder( val view: View) :
        RecyclerView.ViewHolder(view)
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Item>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
    interface OnAdapterListener {
        fun onClick(item: Item)
        fun onUpdate(item: Item)
        fun onDelete(item: Item)
    }
}

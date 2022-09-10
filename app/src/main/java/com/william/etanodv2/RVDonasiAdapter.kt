package com.william.etanodv2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.william.etanodv2.entity.donate

class RVDonasiAdapter(private val data: Array<donate>) : RecyclerView.Adapter<RVDonasiAdapter.viewHolder>() {
    private val images = intArrayOf(R.drawable.donasi1,R.drawable.donasi2,R.drawable.donasi3,R.drawable.donasi4,R.drawable.donasi5,R.drawable.donasi6,R.drawable.donasi7,R.drawable.donasi8,R.drawable.donasi9,R.drawable.donasi10)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_donate, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int){
        val currentItem = data[position]
        holder.judul.text = currentItem.judul
        holder.jumlah.text = currentItem.jumlah.toString()
        holder.durasi.text = currentItem.durasi
        holder.gambar.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val judul : TextView = itemView.findViewById(R.id.judul)
        val jumlah : TextView = itemView.findViewById(R.id.jumlah)
        val durasi : TextView = itemView.findViewById(R.id.durasi)
        val gambar : ImageView = itemView.findViewById(R.id.iv_donasi)
    }
}
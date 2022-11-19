package com.william.etanodv2.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.william.etanodv2.ViewActivity
import com.william.etanodv2.DonateActivity
import com.william.etanodv2.R
import com.william.etanodv2.models.Fundraising
import java.util.*
import kotlin.collections.ArrayList

class DonateAdapter (private var donateList: List<Fundraising>, context: Context): RecyclerView.Adapter<DonateAdapter.ViewHolder>(),
    Filterable {
    private var filteredDonateList: MutableList<Fundraising>
    private val context: Context

    init{
        filteredDonateList = ArrayList(donateList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_donate, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount():Int{
        return filteredDonateList.size
    }

    fun setDonateList(donateList: Array<Fundraising>){
        this.donateList = donateList.toList()
        filteredDonateList = donateList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val donate = filteredDonateList[position]
        holder.tvJudul.text = donate.judul
        holder.tvDana.text = donate.dana
        holder.tvLokasi.text = donate.lokasi
        holder.tvDurasi.text = donate.durasi
        
        holder.cvDonate.setOnClickListener{
            val i = Intent(context, ViewActivity::class.java)
            i.putExtra("id", donate.id)
            if(context is DonateActivity)
                context.startActivityForResult(i, DonateActivity.LAUNCH_LOCATION_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Fundraising> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(donateList)
                }else{
                    for(donate in donateList){
                        if(donate.judul.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(donate)
                    }
                }
                val filterResult = FilterResults()
                filterResult.values = filtered
                return filterResult
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredDonateList.clear()
                filteredDonateList.addAll((filterResults.values as List<Fundraising>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvJudul: TextView
        var tvDana: TextView
        var tvLokasi: TextView
        var tvDurasi: TextView
        var cvDonate: CardView

        init{
            tvJudul = itemView.findViewById(R.id.tv_judul)
            tvDana = itemView.findViewById(R.id.tv_dana)
            tvLokasi = itemView.findViewById(R.id.tv_lokasi)
            tvDurasi = itemView.findViewById(R.id.tv_durasi)
            cvDonate = itemView.findViewById(R.id.cv_donate)
        }
    }

}
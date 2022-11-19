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
import com.william.etanodv2.AddEditActivity
import com.william.etanodv2.FundraisingActivity
import com.william.etanodv2.R
import com.william.etanodv2.models.Fundraising
import java.util.*
import kotlin.collections.ArrayList

class FundraisingAdapter (private var fundraisingList: List<Fundraising>, context: Context): RecyclerView.Adapter<FundraisingAdapter.ViewHolder>(),
    Filterable {
    private var filteredFundraisingList: MutableList<Fundraising>
    private val context: Context

    init{
        filteredFundraisingList = ArrayList(fundraisingList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_fundraising, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount():Int{
        return filteredFundraisingList.size
    }

    fun setFundraisingList(fundraisingList: Array<Fundraising>){
        this.fundraisingList = fundraisingList.toList()
        filteredFundraisingList = fundraisingList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val fundraising = filteredFundraisingList[position]
        holder.tvJudul.text = fundraising.judul
        holder.tvDana.text = fundraising.dana
        holder.tvLokasi.text = fundraising.lokasi
        holder.tvDurasi.text = fundraising.durasi

        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data fundraising ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") { _, _ ->
                    if (context is FundraisingActivity) fundraising.id?.let { it1 ->
                        context.deleteFundraising(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvFundraising.setOnClickListener{
            val i = Intent(context, AddEditActivity::class.java)
            i.putExtra("id", fundraising.id)
            if(context is FundraisingActivity)
                context.startActivityForResult(i, FundraisingActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Fundraising> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(fundraisingList)
                }else{
                    for(fundraising in fundraisingList){
                        if(fundraising.judul.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(fundraising)
                    }
                }
                val filterResult = FilterResults()
                filterResult.values = filtered
                return filterResult
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredFundraisingList.clear()
                filteredFundraisingList.addAll((filterResults.values as List<Fundraising>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvJudul: TextView
        var tvDana: TextView
        var tvLokasi: TextView
        var tvDurasi: TextView
        var btnDelete: ImageButton
        var cvFundraising: CardView

        init{
            tvJudul = itemView.findViewById(R.id.tv_judul)
            tvDana = itemView.findViewById(R.id.tv_dana)
            tvLokasi = itemView.findViewById(R.id.tv_lokasi)
            tvDurasi = itemView.findViewById(R.id.tv_durasi)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvFundraising = itemView.findViewById(R.id.cv_fundraising)
        }
    }

}
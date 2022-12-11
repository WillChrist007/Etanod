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
import com.william.etanodv2.*
import com.william.etanodv2.models.Volunteer
import java.util.*
import kotlin.collections.ArrayList

class OpenAdapter (private var openList: List<Volunteer>, context: Context): RecyclerView.Adapter<OpenAdapter.ViewHolder>(),
    Filterable {
    private var filteredOpenList: MutableList<Volunteer>
    private val context: Context

    init{
        filteredOpenList = ArrayList(openList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_open, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount():Int{
        return filteredOpenList.size
    }

    fun setOpenList(openList: Array<Volunteer>){
        this.openList = openList.toList()
        filteredOpenList = openList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val open = filteredOpenList[position]
        holder.tvJudul.text = open.judul
        holder.tvDeskripsi.text = open.deskripsi
        holder.tvLokasi.text = open.lokasi
        holder.tvWaktu.text = open.waktu

        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data open ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") { _, _ ->
                    if (context is OpenActivity) open.id?.let { it1 ->
                        context.deleteOpen(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvOpen.setOnClickListener{
            val i = Intent(context, AddEditVolunteerActivity::class.java)
            i.putExtra("id", open.id)
            if(context is OpenActivity)
                context.startActivityForResult(i, OpenActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Volunteer> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(openList)
                }else{
                    for(open in openList){
                        if(open.judul.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(open)
                    }
                }
                val filterResult = FilterResults()
                filterResult.values = filtered
                return filterResult
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredOpenList.clear()
                filteredOpenList.addAll((filterResults.values as List<Volunteer>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvJudul: TextView
        var tvDeskripsi: TextView
        var tvLokasi: TextView
        var tvWaktu: TextView
        var btnDelete: ImageButton
        var cvOpen: CardView

        init{
            tvJudul = itemView.findViewById(R.id.tv_judul)
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsi)
            tvLokasi = itemView.findViewById(R.id.tv_lokasi)
            tvWaktu = itemView.findViewById(R.id.tv_waktu)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvOpen = itemView.findViewById(R.id.cv_open)
        }
    }

}
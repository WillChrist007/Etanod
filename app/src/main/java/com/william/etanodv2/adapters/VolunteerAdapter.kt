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

class VolunteerAdapter (private var volunteerList: List<Volunteer>, context: Context): RecyclerView.Adapter<VolunteerAdapter.ViewHolder>(),
    Filterable {
    private var filteredVolunteerList: MutableList<Volunteer>
    private val context: Context

    init{
        filteredVolunteerList = ArrayList(volunteerList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_volunteer, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount():Int{
        return filteredVolunteerList.size
    }

    fun setVolunteerList(volunteerList: Array<Volunteer>){
        this.volunteerList = volunteerList.toList()
        filteredVolunteerList = volunteerList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val volunteer = filteredVolunteerList[position]
        holder.tvJudul.text = volunteer.judul
        holder.tvDeskripsi.text = volunteer.deskripsi
        holder.tvLokasi.text = volunteer.lokasi
        holder.tvWaktu.text = volunteer.waktu


        holder.cvVolunteer.setOnClickListener{
            val i = Intent(context, ViewVolunteerActivity::class.java)
            i.putExtra("id", volunteer.id)
            if(context is VolunteerActivity)
                context.startActivityForResult(i, VolunteerActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Volunteer> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(volunteerList)
                }else{
                    for(volunteer in volunteerList){
                        if(volunteer.judul.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(volunteer)
                    }
                }
                val filterResult = FilterResults()
                filterResult.values = filtered
                return filterResult
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredVolunteerList.clear()
                filteredVolunteerList.addAll((filterResults.values as List<Volunteer>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvJudul: TextView
        var tvDeskripsi: TextView
        var tvLokasi: TextView
        var tvWaktu: TextView
        var cvVolunteer: CardView

        init{
            tvJudul = itemView.findViewById(R.id.tv_judul)
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsi)
            tvLokasi = itemView.findViewById(R.id.tv_lokasi)
            tvWaktu = itemView.findViewById(R.id.tv_waktu)
            cvVolunteer = itemView.findViewById(R.id.cv_volunteer)
        }
    }

}
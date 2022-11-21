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
import com.william.etanodv2.EditProfileActivity
import com.william.etanodv2.UserActivity
import com.william.etanodv2.R
import com.william.etanodv2.models.User1
import java.util.*
import kotlin.collections.ArrayList

class UserAdapter (private var userList: List<User1>, context: Context): RecyclerView.Adapter<UserAdapter.ViewHolder>(),
    Filterable {
    private var filteredUserList: MutableList<User1>
    private val context: Context

    init{
        filteredUserList = ArrayList(userList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount():Int{
        return filteredUserList.size
    }

    fun setUserList(userList: Array<User1>){
        this.userList = userList.toList()
        filteredUserList = userList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val user = filteredUserList[position]
        holder.tvUsername.text = user.username
        holder.tvPassword.text = user.password
        holder.tvEmail.text = user.email
        holder.tvTanggal.text = user.tanggalLahir
        holder.tvTelepon.text = user.telepon

        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data user ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") { _, _ ->
                    if (context is UserActivity) user.id?.let { it1 ->
                        context.deleteUser(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvUser.setOnClickListener{
            val i = Intent(context, EditProfileActivity::class.java)
            i.putExtra("id", user.id)
            if(context is UserActivity)
                context.startActivityForResult(i, UserActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<User1> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(userList)
                }else{
                    for(user in userList){
                        if(user.username.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(user)
                    }
                }
                val filterResult = FilterResults()
                filterResult.values = filtered
                return filterResult
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredUserList.clear()
                filteredUserList.addAll((filterResults.values as List<User1>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvUsername: TextView
        var tvPassword: TextView
        var tvEmail: TextView
        var tvTanggal: TextView
        var tvTelepon: TextView
        var btnDelete: ImageButton
        var cvUser: CardView

        init{
            tvUsername = itemView.findViewById(R.id.tv_username)
            tvPassword = itemView.findViewById(R.id.tv_password)
            tvEmail = itemView.findViewById(R.id.tv_email)
            tvTanggal = itemView.findViewById(R.id.tv_tanggal)
            tvTelepon = itemView.findViewById(R.id.tv_telepon)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvUser = itemView.findViewById(R.id.cv_user)
        }
    }

}
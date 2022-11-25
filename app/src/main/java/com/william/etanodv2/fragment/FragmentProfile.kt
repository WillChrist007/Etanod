package com.william.etanodv2.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.william.etanodv2.EditProfileActivity
import com.william.etanodv2.MainActivity
import com.william.etanodv2.R
import com.william.etanodv2.databinding.ActivityHomeBinding
import com.william.etanodv2.databinding.FragmentProfileBinding
import com.william.etanodv2.room.users.User
import com.william.etanodv2.room.users.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentProfile : Fragment() {
    val dbUser by lazy { UserDB(requireContext()) }

    lateinit var viewUsername: TextView
    lateinit var viewEmail: TextView
    lateinit var viewTelepon: TextView

    lateinit var btnLogout:Button
    lateinit var btnUpdate:Button

    var binding: ActivityHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewUsername=view.findViewById(R.id.profileUsername)
        viewEmail=view.findViewById(R.id.profileEmail)
        viewTelepon=view.findViewById(R.id.profileTelepon)

        btnUpdate=view.findViewById(R.id.btnEdit)
        btnLogout=view.findViewById(R.id.btnLogout)

        val userId= requireActivity().intent.getIntExtra("idLogin",0)
        CoroutineScope(Dispatchers.IO).launch{

            println("user id=" + userId)
            val resultCheckUser: List<User> = dbUser.userDao().getUser(userId)
            println("hasil=" + resultCheckUser)
            //viewUsername.setText("Username : " + resultCheckUser[0].username)
            //viewEmail.setText("Email : " + resultCheckUser[0].email)
            //viewTelepon.setText("Telepon : " + resultCheckUser[0].telepon)

        }

        val tempId = userId

        btnUpdate.setOnClickListener {
            startActivity(
                Intent(requireActivity().applicationContext, EditProfileActivity::class.java)
                    .putExtra("intent_id", tempId)
                    .putExtra("intent_type", 2)
            )
        }

        btnLogout.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Log Out")
            builder.setMessage("Are you sure want to exit?")
                .setPositiveButton("Yes"){ dialog, which ->
                    val moveMain = Intent(activity, MainActivity::class.java)
                    startActivity(moveMain)
                    Toast.makeText(activity, "Anda Berhasil Logout!", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                    requireActivity().finishAndRemoveTask()
                }
                .show()
        }
    }
}
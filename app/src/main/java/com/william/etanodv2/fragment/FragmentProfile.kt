package com.william.etanodv2.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.william.etanodv2.*
import com.william.etanodv2.databinding.ActivityHomeBinding
import com.william.etanodv2.models.User
import com.william.etanodv2.room.users.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentProfile : Fragment() {
    //val dbUser by lazy { UserDB(requireContext()) }

    lateinit var btnLogout:Button
    lateinit var btnView:Button

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

        btnView=view.findViewById(R.id.btnView)
        btnLogout=view.findViewById(R.id.btnLogout)

        btnView.setOnClickListener {
            startActivity(
                Intent(requireActivity().applicationContext, EditProfileActivity::class.java)
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
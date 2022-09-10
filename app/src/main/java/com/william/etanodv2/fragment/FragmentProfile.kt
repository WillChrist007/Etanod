package com.william.etanodv2.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.william.etanodv2.MainActivity
import com.william.etanodv2.R

class FragmentProfile : Fragment() {

   private lateinit var logoutBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        init(view)

        logoutBtn.setOnClickListener {
            val moveMain = Intent(activity, MainActivity::class.java)
            startActivity(moveMain)
            Toast.makeText(activity, "Anda Berhasil Logout!", Toast.LENGTH_SHORT).show()
            activity?.finish()
        }
        return view
    }

    private fun init(view: View) {
        logoutBtn = view.findViewById<Button>(R.id.btnLogout)
    }

}
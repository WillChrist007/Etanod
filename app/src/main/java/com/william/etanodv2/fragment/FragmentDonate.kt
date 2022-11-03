package com.william.etanodv2.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.william.etanodv2.EditProfileActivity
import com.william.etanodv2.R
import com.william.etanodv2.RVDonasiAdapter
import com.william.etanodv2.ScanQRActivity
import com.william.etanodv2.entity.donate
import com.william.etanodv2.room.Constant
import kotlinx.android.synthetic.main.fragment_donate.*

class FragmentDonate : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_donate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        setupListener()

        val layoutManager = LinearLayoutManager(context)
        val adapter : RVDonasiAdapter = RVDonasiAdapter(donate.listOfDonate)

        val rvDonate : RecyclerView = view.findViewById(R.id.rv_donate)

        rvDonate.layoutManager = layoutManager

        rvDonate.setHasFixedSize(true)

        rvDonate.adapter =adapter
    }

    private fun setupListener(){
        imgScan.setOnClickListener {
            startActivity(
                Intent(requireActivity().applicationContext, ScanQRActivity::class.java)
            )
        }
    }
}
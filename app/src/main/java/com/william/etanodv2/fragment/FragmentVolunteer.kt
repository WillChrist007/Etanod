package com.william.etanodv2.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.william.etanodv2.OpenActivity
import com.william.etanodv2.R
import com.william.etanodv2.VolunteerActivity
import kotlinx.android.synthetic.main.fragment_volunteer.*


class FragmentVolunteer : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_volunteer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        setupListener()
    }

    private fun setupListener(){
        openButton.setOnClickListener {
            startActivity(
                Intent(requireActivity().applicationContext, OpenActivity::class.java)
            )
        }
        daftarButton.setOnClickListener {
            startActivity(
                Intent(requireActivity().applicationContext, VolunteerActivity::class.java)
            )
        }
    }
}
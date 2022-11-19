package com.william.etanodv2.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.william.etanodv2.*
import kotlinx.android.synthetic.main.fragment_home.*

class FragmentHome : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        setupListener()
    }

    private fun setupListener(){
        donateButton.setOnClickListener {
            startActivity(
                Intent(requireActivity().applicationContext, DonateActivity::class.java)
            )
        }
        fundraisingButton.setOnClickListener {
            startActivity(
                Intent(requireActivity().applicationContext, FundraisingActivity::class.java)
            )
        }
    }
}
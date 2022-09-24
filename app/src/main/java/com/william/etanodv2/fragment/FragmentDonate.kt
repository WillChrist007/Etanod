package com.william.etanodv2.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.william.etanodv2.R
import com.william.etanodv2.RVDonasiAdapter
import com.william.etanodv2.entity.donate

class FragmentDonate : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_donate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVDonasiAdapter = RVDonasiAdapter(donate.listOfDonate)

        val rvDonate : RecyclerView = view.findViewById(R.id.rv_donate)

        rvDonate.layoutManager = layoutManager

        rvDonate.setHasFixedSize(true)

        rvDonate.adapter =adapter
    }
}
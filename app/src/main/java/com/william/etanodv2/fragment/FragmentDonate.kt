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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentDonate.newInstance] factory method to
 * create an instance of this fragment.
 */
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
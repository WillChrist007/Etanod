package com.william.etanodv2.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.william.etanodv2.EditActivity
import com.william.etanodv2.RVFundraisingAdapter
import com.william.etanodv2.databinding.FragmentFundraisingBinding
import com.william.etanodv2.room.Constant
import com.william.etanodv2.room.items.Item
import com.william.etanodv2.room.items.ItemDB
import kotlinx.android.synthetic.main.fragment_fundraising.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentFundraising : Fragment() {
    val dbItem by lazy { ItemDB(requireActivity()) }
    lateinit var itemAdapter : RVFundraisingAdapter
    private var _binding: FragmentFundraisingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFundraisingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupRecyclerView()
    }
    
    private fun setupRecyclerView(){
        itemAdapter = RVFundraisingAdapter(arrayListOf(), object : RVFundraisingAdapter.OnAdapterListener{

            override fun onClick(item: Item) {
                intentEdit(item.id, Constant.TYPE_READ)
            }

            override fun onUpdate(item: Item) {
                intentEdit(item.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(item: Item) {
                deleteDialog(item)
            }
        })
        binding.listNote.apply {
            layoutManager = LinearLayoutManager(requireActivity().applicationContext)
            adapter = itemAdapter
        }
    }

    private fun deleteDialog(item: Item){
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You sure to delete ${item.judul}?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialogInterface, i ->
                dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener{ dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    dbItem.itemDao().deleteItem(item)
                    loadData()
                }
            })
        }
        alertDialog.show()
    }

    override fun onStart(){
        super.onStart()
        loadData()
    }

    fun loadData(){
        CoroutineScope(Dispatchers.IO).launch {
            val items = dbItem.itemDao().getItems()
            Log.d("FragmentFundraising", "dbResponse: $items")
            withContext(Dispatchers.Main){
                itemAdapter.setData(items)
            }
        }
    }

    fun setupListener(){
        binding.buttonCreate.setOnClickListener{
            intentEdit(0, Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(itemId: Int, intentType: Int){
        startActivity(
            Intent(requireActivity().applicationContext, EditActivity::class.java)
                .putExtra("intent_id", itemId)
                .putExtra("intent_type", intentType)
        )
    }
}
package com.william.etanodv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.william.etanodv2.room.Constant
import com.william.etanodv2.room.items.Item
import com.william.etanodv2.room.items.ItemDB
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    val dbItem by lazy { ItemDB(this) }
    private var itemId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setupView()
        setupListener()


        Toast.makeText(this, itemId.toString(), Toast.LENGTH_SHORT).show()
    }
    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_CREATE -> {
                button_update.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
                getItem()
            }
            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getItem()
            }
        }
    }
    private fun setupListener() {
        button_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                dbItem.itemDao().addItem(
                    Item(0,edit_judul.text.toString(),
                        edit_jumlah.text.toString(),
                        edit_durasi.text.toString())
                )
                finish()
            }
        }
        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                dbItem.itemDao().updateItem(
                    Item(itemId,edit_judul.text.toString(),
                        edit_jumlah.text.toString(),
                        edit_durasi.text.toString())
                )
                finish()
            }
        }
    }

    fun getItem() {
        itemId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val items = dbItem.itemDao().getItem(itemId)[0]
            edit_judul.setText(items.judul)
            edit_jumlah.setText(items.jumlah)
            edit_durasi.setText(items.durasi)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
package com.william.etanodv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val loginBtn: Button = findViewById(R.id.selector_login)
        val RegisterBtn: Button = findViewById(R.id.selector_Register)

        loginBtn.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Akun Belum Dibuat")
            builder.setMessage("Silahkan Buat Akun terlebih Dahulu!")
                .setPositiveButton("oke"){ dialog, which -> }
                .show()
        }

        RegisterBtn.setOnClickListener {
            val moveRegister = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(moveRegister)
        }
    }
}
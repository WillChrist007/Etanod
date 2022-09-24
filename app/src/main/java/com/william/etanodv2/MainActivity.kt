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
            val moveRegister = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(moveRegister)
        }

        RegisterBtn.setOnClickListener {
            val moveRegister = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(moveRegister)
        }
    }
}
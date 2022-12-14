package com.william.etanodv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.william.etanodv2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnLogin?.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

        binding?.btnRegister?.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
        }

        binding?.btnTAC?.setOnClickListener {
            startActivity(Intent(this@MainActivity, TermConditionActivity::class.java))
        }
    }
}
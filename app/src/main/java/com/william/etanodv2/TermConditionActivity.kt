package com.william.etanodv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.william.etanodv2.databinding.ActivityMainBinding
import com.william.etanodv2.databinding.ActivityTermConditionBinding

class TermConditionActivity : AppCompatActivity() {
    var binding : ActivityTermConditionBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityTermConditionBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnCetak?.setOnClickListener {

        }

        binding?.btnSetuju?.setOnClickListener {
            startActivity(Intent(this@TermConditionActivity, MainActivity::class.java))
        }
    }
}
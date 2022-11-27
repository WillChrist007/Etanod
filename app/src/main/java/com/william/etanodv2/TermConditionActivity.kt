package com.william.etanodv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.shashank.sony.fancytoastlib.FancyToast
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
            FancyToast.makeText(this, "Berhasil Mengunduh !", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, R.drawable.etanod, false).show()
        }

        binding?.btnSetuju?.setOnClickListener {
            FancyToast.makeText(this, "Selamat Menggunakan Aplikasi !", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, R.drawable.etanod, false).show()
            startActivity(Intent(this@TermConditionActivity, MainActivity::class.java))
        }
    }
}
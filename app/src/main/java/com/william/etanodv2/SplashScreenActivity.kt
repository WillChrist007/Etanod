package com.william.etanodv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import java.lang.Boolean

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        val prefs = getSharedPreferences("splash_screen_prefernce", MODE_PRIVATE)
        if (!prefs.getBoolean("bypass_boolean", false)) {
            val editor = getSharedPreferences("splash_screen_prefernce", MODE_PRIVATE).edit()
            editor.putBoolean("bypass_boolean", true)
            editor.apply()
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            },3000)
        }else{
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            finish()
        }
    }

}
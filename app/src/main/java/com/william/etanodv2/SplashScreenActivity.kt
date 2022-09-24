package com.william.etanodv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import java.lang.Boolean

class SplashScreenActivity : AppCompatActivity() {
    lateinit var splashScreen: RelativeLayout
    var prevStarted = "yes"

    override fun onResume() {
        super.onResume()

        val sharedpreferences = getSharedPreferences("Etanod", Context.MODE_PRIVATE)
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            val editor = sharedpreferences.edit()
            editor.putBoolean(prevStarted, Boolean.TRUE)
            editor.apply()
            val splashScreen = findViewById<RelativeLayout>(R.id.splash_screen)
        }else{
            moveToSecondary()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash_screen)

        val handler = Handler()
        handler.postDelayed(Runnable {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }, 3000L)
    }

    fun moveToSecondary() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
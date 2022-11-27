package com.william.etanodv2

import alirezat775.lib.downloader.Downloader
import alirezat775.lib.downloader.core.OnDownloadListener
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.shashank.sony.fancytoastlib.FancyToast
import com.william.etanodv2.databinding.ActivityMainBinding
import com.william.etanodv2.databinding.ActivityTermConditionBinding
import java.io.File

class TermConditionActivity : AppCompatActivity() {
    var binding : ActivityTermConditionBinding? = null

    private var downloader: Downloader? = null
    private val TAG: String = this::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityTermConditionBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnCetak?.setOnClickListener {
            getDownloader()
            downloader?.download()
            FancyToast.makeText(this, "Berhasil Mengunduh !", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, R.drawable.etanod, false).show()
        }

        binding?.btnSetuju?.setOnClickListener {
            FancyToast.makeText(this, "Selamat Menggunakan Aplikasi !", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, R.drawable.etanod, false).show()
            startActivity(Intent(this@TermConditionActivity, MainActivity::class.java))
        }
    }

    private fun getDownloader() {
        downloader = Downloader.Builder(
            this,
            "https://i.ibb.co/60t6Rr4/Term-And-Condition-Etanod.png"
        ).downloadListener(object : OnDownloadListener {
            override fun onStart() {
                Log.d(TAG, "onStart")
            }

            override fun onPause() {
                Log.d(TAG, "onPause")
            }

            override fun onResume() {
                Log.d(TAG, "onResume")
            }

            override fun onProgressUpdate(percent: Int, downloadedSize: Int, totalSize: Int) {
                Log.d(
                    TAG,
                    "onProgressUpdate: percent --> $percent downloadedSize --> $downloadedSize totalSize --> $totalSize "
                )
            }

            override fun onCompleted(file: File?) {
                Log.d(TAG, "onCompleted: file --> $file")
            }

            override fun onFailure(reason: String?) {
                Log.d(TAG, "onFailure: reason --> $reason")
            }

            override fun onCancel() {
                Log.d(TAG, "onCancel")
            }
        }).build()
    }
}
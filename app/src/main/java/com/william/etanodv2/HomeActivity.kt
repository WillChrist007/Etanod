package com.william.etanodv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.william.etanodv2.fragment.FragmentHome
import com.william.etanodv2.fragment.FragmentScanner
import com.william.etanodv2.fragment.FragmentProfile

class HomeActivity : AppCompatActivity() {
    private val homeFragment = FragmentHome()
    private val scannerFragment = FragmentScanner()
    private val profileFragment = FragmentProfile()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        replaceFragment(homeFragment)
        val bottom: BottomNavigationView = findViewById(R.id.select_menu)
        bottom.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(homeFragment)
                R.id.scanner->replaceFragment(scannerFragment)
                R.id.profile->replaceFragment(profileFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

}
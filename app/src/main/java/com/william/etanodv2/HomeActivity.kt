package com.william.etanodv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.william.etanodv2.fragment.FragmentDonate
import com.william.etanodv2.fragment.FragmentFundraising
import com.william.etanodv2.fragment.FragmentProfile

class HomeActivity : AppCompatActivity() {
    private val donateFragment = FragmentDonate()
    private val fundraisingFragment = FragmentFundraising()
    private val profileFragment = FragmentProfile()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        replaceFragment(donateFragment)
        val bottom: BottomNavigationView = findViewById(R.id.select_menu)
        bottom.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.donate -> replaceFragment(donateFragment)
                R.id.fundraising->replaceFragment(fundraisingFragment)
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
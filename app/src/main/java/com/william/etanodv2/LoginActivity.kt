package com.william.etanodv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    lateinit var regBundle: Bundle

    lateinit var tempUsername: String
    lateinit var tempPassword: String
    lateinit var tempEmail: String
    lateinit var tempTanggal: String
    lateinit var tempTelepon: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        inputUsername = findViewById(R.id.username)
        inputPassword = findViewById(R.id.password)
        val loginBtn: Button = findViewById(R.id.btnLogin)

        getBundle()

        loginBtn.setOnClickListener (View.OnClickListener {
            var cekLogin = false
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if(username.isEmpty()){
                inputUsername.setError("Username Tidak Boleh Kosong")
                cekLogin = false
            }

            if(password.isEmpty()){
                inputPassword.setError("Password Tidak Boleh Kosong")
                cekLogin = false
            }

            if(username == tempUsername && password == tempPassword){
                cekLogin = true
            }else{
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
                builder.setTitle("Username atau Password")
                builder.setMessage("Silahkan Isi Username dan Password Dengan Benar!")
                    .setPositiveButton("oke"){ dialog, which -> }
                    .show()
            }
            if(!cekLogin)return@OnClickListener
            val moveHome = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(moveHome)
        })
    }

    fun getBundle(){
        regBundle = intent.getBundleExtra("register")!!

        tempUsername = regBundle.getString("username")!!
        tempPassword = regBundle.getString("password")!!
        tempEmail = regBundle.getString("email")!!
        tempTanggal = regBundle.getString("tanggal")!!
        tempTelepon = regBundle.getString("telepon")!!
        inputUsername.getEditText()?.setText(tempUsername)
    }
}
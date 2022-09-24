package com.william.etanodv2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.william.etanodv2.room.users.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout

    lateinit var tempUsername: String
    lateinit var tempPassword: String

    val dbUser by lazy { UserDB(this) }

    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passwordK = "passwordKey"
    var sharedPreferencesRegister: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        inputUsername = findViewById(R.id.username)
        inputPassword = findViewById(R.id.password)
        val btnLogin_click: Button = findViewById(R.id.btnLogin)

        getBundle()
        tempUsername=""
        tempPassword=""
        
        btnLogin_click.setOnClickListener (View.OnClickListener {
            var cekLogin = false

            getPembanding(inputUsername.editText?.text.toString())

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
                var cekUsername: String = tempUsername
                var cekPassword: String = tempPassword
                val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
                editor.putString(usernameK, cekUsername)
                editor.putString(passwordK, cekPassword)
                editor.apply()
            }else{
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
                builder.setTitle("USername atau Password Salah")
                builder.setMessage("Silahakan Isi Username dan Password Dengan Benar!")
                    .setPositiveButton("Yes"){ dialog, which ->
                    }
                    .show()
            }
            if(!cekLogin)return@OnClickListener
            val moveHomer = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(moveHomer)
        })
    }

    fun getBundle(){

        sharedPreferencesRegister = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        if (sharedPreferencesRegister!!.contains(usernameK)){
            inputUsername.getEditText()?.setText(sharedPreferencesRegister!!.getString(usernameK, ""))
        }
        if (sharedPreferencesRegister!!.contains(passwordK)){
            inputPassword.getEditText()?.setText(sharedPreferencesRegister!!.getString(passwordK, ""))
        }
    }

    fun getPembanding(value: String){
        CoroutineScope(Dispatchers.Main).launch {
            val user = dbUser.userDao().getUser(value)[0]
            tempUsername = user.username
            tempPassword = user.password
            Toast.makeText(applicationContext, user.username, Toast.LENGTH_SHORT).show()
        }

    }
}
package com.william.etanodv2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.shashank.sony.fancytoastlib.FancyToast
import com.william.etanodv2.room.users.User
import com.william.etanodv2.room.users.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout

    private lateinit var btnLogin: Button

    private lateinit var mainLayout: ConstraintLayout

    val dbUser by lazy { UserDB(this) }

    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passwordK = "passwordKey"
    var sharedPreferencesRegister: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        mainLayout = findViewById(R.id.mainLayout)

        inputUsername = findViewById(R.id.username)
        inputPassword = findViewById(R.id.password)
        btnLogin = findViewById(R.id.btnLogin)

        getBundle()


        btnLogin.setOnClickListener (View.OnClickListener {
            var cekLogin = false

            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if(username.isEmpty()){
                FancyToast.makeText(this@LoginActivity, "Username Tidak Boleh Kosong !", FancyToast.LENGTH_LONG, FancyToast.ERROR, R.drawable.etanod, false).show()
                cekLogin = false
            }

            else if(password.isEmpty()){
                FancyToast.makeText(this@LoginActivity, "Password Tidak Boleh Kosong !", FancyToast.LENGTH_LONG, FancyToast.ERROR, R.drawable.etanod, false).show()
                cekLogin = false
            }

            else if(username == "admin" && password == "admin") {
                val intent=Intent(this@LoginActivity, UserActivity::class.java)
                startActivity(intent)
            }

            else if(!username.isEmpty() && !password.isEmpty()){
                CoroutineScope(Dispatchers.IO).launch {
                    var resultCheckUser: List<User> = dbUser.userDao().checkUser(username,password)
                    println("hasil: " + resultCheckUser)

                    if(resultCheckUser.isNullOrEmpty()){
                        Snackbar.make(mainLayout,"Username atau Password Salah!", Snackbar.LENGTH_LONG).show()
                        return@launch
                    }

                    else if(resultCheckUser[0].username.equals(username) && resultCheckUser[0].password.equals(password)){
                        cekLogin=true
                        val intent=Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.putExtra("usernameLogin",username)
                        intent.putExtra("idLogin",resultCheckUser[0].id)


                        val editor: SharedPreferences.Editor= sharedPreferencesRegister!!.edit()
                        editor.putString(usernameK,username)
                        editor.putString(passwordK,password)
                        editor.apply()

                        startActivity(intent)
                    }
                }
            }
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

}
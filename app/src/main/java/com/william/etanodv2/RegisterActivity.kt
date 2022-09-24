package com.william.etanodv2

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import com.william.etanodv2.databinding.ActivityRegisterBinding
import com.william.etanodv2.room.users.User
import com.william.etanodv2.room.users.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    var kalender = Calendar.getInstance()

    val dbUser by lazy { UserDB(this) }

    private lateinit var binding: ActivityRegisterBinding

    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passwordK = "passwordKey"
    var sharedPreferencesRegister: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val moveLogin = Intent(this, LoginActivity::class.java)

        sharedPreferencesRegister = getSharedPreferences(myPreference, Context.MODE_PRIVATE)

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                kalender.set(Calendar.YEAR, year)
                kalender.set(Calendar.MONTH, monthOfYear)
                kalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateEditText()
            }
        }

        binding?.etTanggal?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View) {
                DatePickerDialog(this@RegisterActivity,
                    dateSetListener,
                    kalender.get(Calendar.YEAR),
                    kalender.get(Calendar.MONTH),
                    kalender.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        binding?.btnRegistration?.setOnClickListener (View.OnClickListener{
            var checkRegister = false

            val inputUsername: String = binding?.username?.getEditText()?.getText().toString()
            val inputPassword: String = binding?.password?.getEditText()?.getText().toString()
            val inputEmail: String = binding?.email?.getEditText()?.getText().toString()
            val inputTanggal: String = binding?.tanggalLahir?.getEditText()?.getText().toString()
            val inputTelepon: String = binding?.telepon?.getEditText()?.getText().toString()

            if(inputUsername.isEmpty()){
                binding?.username?.setError("Username Tidak Boleh Kosong")
                checkRegister = false
            }

            if(inputPassword.isEmpty()){
                binding?.password?.setError("Password Tidak Boleh Kosong")
                checkRegister = false
            }

            if(inputEmail.isEmpty()){
                binding?.email?.setError("Email Tidak Boleh Kosong")
                checkRegister = false
            }

            if(inputTanggal.isEmpty()){
                binding?.tanggalLahir?.setError("Tanggal Lahir Tidak Boleh Kosong")
                checkRegister = false
            }

            if(inputTelepon.isEmpty()){
                binding?.telepon?.setError("No Tlp Tidak Boleh Kosong")
                checkRegister = false
            }else if(inputTelepon.length < 12){
                binding?.telepon?.setError("Panjang No Tlp harus > 12")
                checkRegister = false
            }

            if(!inputUsername.isEmpty() && !inputPassword.isEmpty() && !inputEmail.isEmpty() && !inputTanggal.isEmpty() && !inputTelepon.isEmpty() && inputTelepon.length == 12){
                checkRegister = true

                CoroutineScope(Dispatchers.IO).launch {
                    dbUser.userDao().addUser(
                        User(0, inputEmail, inputUsername, inputPassword, inputTanggal, inputTelepon)
                    )
                    finish()
                }

                var strUserName: String = binding.username.editText?.text.toString().trim()
                var strPass: String = binding.password.editText?.text.toString().trim()
                val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
                editor.putString(usernameK, strUserName)
                editor.putString(passwordK, strPass)
                editor.apply()
            }

            if(!checkRegister)return@OnClickListener
            startActivity(moveLogin)
        })
    }
    private fun updateEditText(){
        var temp : String
        val dateFormat = "MM/dd/yyyy"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.US)
        temp = simpleDateFormat.format(kalender.getTime())
        binding?.tanggalLahir?.getEditText()?.setText(temp)
    }

}
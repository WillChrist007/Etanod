package com.william.etanodv2

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var inputEmail: TextInputLayout
    private lateinit var inputTanggal: TextInputLayout
    private lateinit var editTanggal : EditText
    private lateinit var inputTelepon: TextInputLayout
    var kalender = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        inputUsername = findViewById(R.id.username)
        inputPassword = findViewById(R.id.password)
        inputEmail = findViewById(R.id.email)
        inputTanggal = findViewById(R.id.tanggal_lahir)
        editTanggal =  findViewById(R.id.etTanggal)
        inputTelepon = findViewById(R.id.tlp)
        
        val registerBtn: Button = findViewById(R.id.btnRegistration)
        val moveLogin = Intent(this, LoginActivity::class.java)
        val regBundle = Bundle()
        regBundle.putString("username", "")
        regBundle.putString("password", "")
        regBundle.putString("email", "")
        regBundle.putString("tanggal", "")
        regBundle.putString("telepon", "")
        moveLogin.putExtra("register", regBundle)

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                kalender.set(Calendar.YEAR, year)
                kalender.set(Calendar.MONTH, monthOfYear)
                kalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateEditText()
            }
        }

        editTanggal.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View) {
                DatePickerDialog(this@RegisterActivity,
                    dateSetListener,
                    kalender.get(Calendar.YEAR),
                    kalender.get(Calendar.MONTH),
                    kalender.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        registerBtn.setOnClickListener (View.OnClickListener{
            var checkRegister = false
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()
            val email: String = inputEmail.getEditText()?.getText().toString()
            val tanggal: String = inputTanggal.getEditText()?.getText().toString()
            val telepon: String = inputTelepon.getEditText()?.getText().toString()

            if(username.isEmpty()){
                inputUsername.setError("Username Tidak Boleh Kosong")
                checkRegister = false
            }

            if(password.isEmpty()){
                inputPassword.setError("Password Tidak Boleh Kosong")
                checkRegister = false
            }


            if(email.isEmpty()){
                inputEmail.setError("Email Tidak Boleh Kosong")
                checkRegister = false
            }

            if(tanggal.isEmpty()){
                inputTanggal.setError("Tanggal Lahir Tidak Boleh Kosong")
                checkRegister = false
            }

            if(telepon.isEmpty()){
                inputTelepon.setError("Nomor Telepon Tidak Boleh Kosong")
                checkRegister = false
            }else if(telepon.length < 12){
                inputTelepon.setError("Nomor Telepon Tidak valid (12 digit)")
                checkRegister = false
            }

            if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !tanggal.isEmpty() && !telepon.isEmpty() && telepon.length == 12){
                checkRegister = true
                regBundle.putString("username", username)
                regBundle.putString("password", password)
                regBundle.putString("email", email)
                regBundle.putString("tanggal", tanggal)
                regBundle.putString("telepon", telepon)
                moveLogin.putExtra("register", regBundle)
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@RegisterActivity)
                builder.setTitle("Berhasil")
                builder.setMessage("Akun Anda Berhasil Dibuat!")
                    .setPositiveButton("oke"){ dialog, which -> }
                    .show()
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
        inputTanggal.getEditText()?.setText(temp)
    }
}
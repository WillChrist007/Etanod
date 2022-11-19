package com.william.etanodv2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.william.etanodv2.databinding.ActivityEditProfileBinding
import com.william.etanodv2.room.Constant
import com.william.etanodv2.room.users.User
import com.william.etanodv2.room.users.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    lateinit var tempUsername: String
    lateinit var tempPassword: String

    var tempId: Int = 0
    var dataUser: Int = 0

    val dbUser by lazy { UserDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setTitle("Edit Profile")

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        tempUsername = ""
        tempPassword = ""
        setupView()

        binding.btnSave.setOnClickListener{
            val inputUsername: String = binding?.editUsername?.getEditText()?.getText().toString()
            val inputPassword: String = binding?.editPassword?.getEditText()?.getText().toString()
            val inputEmail: String = binding?.editEmail?.getEditText()?.getText().toString()
            val inputTanggal: String = binding?.editTanggal?.getEditText()?.getText().toString()
            val inputTelepon: String = binding?.editTelepon?.getEditText()?.getText().toString()

            if(inputUsername.isEmpty()){
                binding?.editTanggal?.setError("Username Tidak Boleh Kosong")
            }

            if(inputPassword.isEmpty()){
                binding?.editPassword?.setError("Password Tidak Boleh Kosong")
            }

            if(inputEmail.isEmpty()){
                binding?.editEmail?.setError("Email Tidak Boleh Kosong")
            }

            if(inputTanggal.isEmpty()){
                binding?.editTanggal?.setError("Tanggal Lahir Tidak Boleh Kosong")
            }

            if(inputTelepon.isEmpty()){
                binding?.editTelepon?.setError("Nomor Telepon Tidak Boleh Kosong")
            }else if(inputTelepon.length < 12){
                binding?.editTelepon?.setError("Nomor Telepon TIdak Valid")
            }

            if(!inputUsername.isEmpty() && !inputPassword.isEmpty() && !inputEmail.isEmpty() && !inputTanggal.isEmpty() && !inputTelepon.isEmpty() && inputTelepon.length >= 12) {
                CoroutineScope(Dispatchers.IO).launch {
                    dbUser.userDao().updateUser(
                        User(
                            tempId,
                            binding.editUsername.editText?.text.toString(),
                            binding.editPassword.editText?.text.toString(),
                            binding.editEmail.editText?.text.toString(),
                            binding.editTanggal.editText?.text.toString(),
                            binding.editTelepon.editText?.text.toString()
                        )
                    )
                    finish()
                    val intent= Intent(this@EditProfileActivity, HomeActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    fun setupView() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType) {
            Constant.TYPE_UPDATE -> {
                getUser()
            }
        }
    }

    fun getUser(){
        dataUser = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.Main).launch {
            val user = dbUser.userDao().getUser(dataUser)[0]
            binding.editEmail.editText?.setText(user.email)
            binding.editUsername.editText?.setText(user.username)
            binding.editTelepon.editText?.setText(user.telepon)
            binding.editTanggal.editText?.setText(user.tanggalLahir)
            tempId = user.id
            tempPassword = user.password
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
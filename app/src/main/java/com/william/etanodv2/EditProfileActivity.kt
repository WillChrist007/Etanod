package com.william.etanodv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    lateinit var tempTanggal: String
    var tempId: Int = 0
    var dataUser: Int = 0

    val dbUser by lazy { UserDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setTitle("Editing Profile")

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        tempUsername = ""
        tempPassword = ""
        tempTanggal = ""
        setupView()
        setupListenerEditUser()

    }

    fun setupListenerEditUser(){

        binding.btnSave.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                dbUser.userDao().updateUser(
                    User(tempId, binding.email.editText?.text.toString(), binding.username.editText?.text.toString(),
                        binding.password.editText?.text.toString(), tempTanggal, binding.telepon.editText?.text.toString())
                )
                finish()
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
            val user = dbUser.userDao().getUser2(dataUser)[0]
            binding.email.editText?.setText(user.email)
            binding.username.editText?.setText(user.username)
            binding.telepon.editText?.setText(user.telepon)
            tempId = user.id
            tempTanggal = user.tanggalLahir
            tempPassword = user.password
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
package com.william.etanodv2.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.william.etanodv2.EditProfileActivity
import com.william.etanodv2.MainActivity
import com.william.etanodv2.R
import com.william.etanodv2.databinding.FragmentProfileBinding
import com.william.etanodv2.room.users.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentProfile : Fragment() {
    val dbUser by lazy { UserDB(requireActivity()) }

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    var tampilUsername: String? = null
    var tampilPassword: String? = null
    var tempId: Int = 0

    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passK = "passKey"
    var sharedPreferencesProfile: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        getspData()
        getProfileData(tampilUsername.toString())

        binding.btnLogout.setOnClickListener{
            logout()
        }

        binding.btnEdit.setOnClickListener{
            intentEdit(tempId, 2)
        }
    }

    fun getspData(){
        sharedPreferencesProfile = this.getActivity()?.getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        if (sharedPreferencesProfile!!.contains(usernameK)){
            tampilUsername = sharedPreferencesProfile!!.getString(usernameK, "")
        }
        if (sharedPreferencesProfile!!.contains(passK)){
            tampilPassword = sharedPreferencesProfile!!.getString(passK, "")
        }
    }

    private fun getProfileData(str: String){
        CoroutineScope(Dispatchers.Main).launch {
            val user = dbUser.userDao().getUser(str)[0]
            binding.profileUsername.setText(user.username)
            binding.profileEmail.setText(user.email)
            binding.profileTelepon.setText(user.telepon)
            tempId = user.id
        }
    }

    private fun logout(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Log Out")
        builder.setMessage("Are you sure want to exit?")
            .setPositiveButton("Yes"){ dialog, which ->
                requireActivity().finishAndRemoveTask()
            }
            .show()
    }

    fun intentEdit(id_input: Int, intentType: Int){
        startActivity(
            Intent(requireActivity().applicationContext, EditProfileActivity::class.java)
                .putExtra("intent_id", id_input)
                .putExtra("intent_type", intentType)
        )
    }

}
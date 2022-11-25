package com.william.etanodv2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.william.etanodv2.api.UserApi
import com.william.etanodv2.models.Fundraising
import com.william.etanodv2.models.User1
import com.william.etanodv2.room.users.User
import com.william.etanodv2.room.users.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class EditProfileActivity : AppCompatActivity() {

    private var etEditUsername: EditText? = null
    private var etEditPassword: EditText? = null
    private var etEditEmail: EditText? = null
    private var etEditTanggal: EditText? = null
    private var etEditTelepon: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    val dbUser by lazy { UserDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        queue = Volley.newRequestQueue(this)
        etEditUsername = findViewById(R.id.edit_username)
        etEditPassword = findViewById(R.id.edit_password)
        etEditEmail = findViewById(R.id.edit_email)
        etEditTanggal = findViewById(R.id.edit_tanggal)
        etEditTelepon = findViewById(R.id.edit_telepon)
        layoutLoading = findViewById(R.id.layout_loading)

        val btnSave = findViewById<Button>(R.id.btnSave)
        val id = intent.getLongExtra("id", -1)
        if(id==-1L) {
            btnSave.setOnClickListener { createUser() }
        } else {
            getUserById(id)

            btnSave.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    dbUser.userDao().updateUser(
                        User(
                            id, etEditUsername.toString(), etEditPassword.toString(),
                            etEditEmail.toString(), etEditTanggal.toString(),
                            etEditTelepon.toString()
                        )
                    )
                }
                updateUser(id)
            }
        }

    }

    private fun getUserById(id: Long) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, UserApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val user = gson.fromJson(jsonObject.getJSONArray("data")[0].toString(), User1::class.java)

                etEditUsername!!.setText(user.username)
                etEditPassword!!.setText(user.password)
                etEditEmail!!.setText(user.email)
                etEditTanggal!!.setText(user.tanggalLahir)
                etEditTelepon!!.setText(user.telepon)

                Toast.makeText(this@EditProfileActivity, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)

                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@EditProfileActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@EditProfileActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    private fun createUser(){
        setLoading(true)

        val user = User1(
            etEditUsername!!.text.toString(),
            etEditPassword!!.text.toString(),
            etEditEmail!!.text.toString(),
            etEditTanggal!!.text.toString(),
            etEditTelepon!!.text.toString()
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, UserApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val user = gson.fromJson(jsonObject.getJSONArray("data")[0].toString(), User1::class.java)

                if(user != null)
                    Toast.makeText(this@EditProfileActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@EditProfileActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@EditProfileActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                override fun getParams(): MutableMap<String, String>? {
                    val params = HashMap<String, String>()
                    params["username"] = user.username
                    params["password"] = user.password
                    params["email"] = user.email
                    params["tanggalLahir"] = user.tanggalLahir
                    params["telepon"] = user.telepon
                    return params
                }
            }
        queue!!.add(stringRequest)
    }

    private fun updateUser(id: Long) {
        setLoading(true)

        val user = User1(
            etEditUsername!!.text.toString(),
            etEditPassword!!.text.toString(),
            etEditEmail!!.text.toString(),
            etEditTanggal!!.text.toString(),
            etEditTelepon!!.text.toString()
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, UserApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()

                var user = gson.fromJson(response, User1::class.java)

                if(user != null)
                    Toast.makeText(this@EditProfileActivity, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@EditProfileActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@EditProfileActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }

            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = user.username
                params["password"] = user.password
                params["email"] = user.email
                params["tanggalLahir"] = user.tanggalLahir
                params["telepon"] = user.telepon
                return params
            }
        }
        queue!!.add(stringRequest)
    }

    private fun setLoading(isLoading: Boolean) {
        if(isLoading) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }
}
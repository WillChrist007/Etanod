package com.william.etanodv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.william.etanodv2.api.UserApi
import com.william.etanodv2.databinding.ActivityLoginBinding
import com.william.etanodv2.models.User
import com.william.etanodv2.room.users.UserDB
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class LoginActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private var queue: RequestQueue? = null
    var binding : ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        queue = Volley.newRequestQueue(this)
        setContentView(binding?.root)

        inputUsername = findViewById(R.id.username)
        inputPassword = findViewById(R.id.password)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        var user = "admin"
        var pass = "admin"

        val bundle = intent.extras

        if(bundle!=null){
            user = bundle.getString("username").toString()
            pass = bundle.getString("password").toString()
            inputPassword.editText?.setText(pass)
            inputUsername.editText?.setText(user)
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = false

            var username = inputUsername.editText?.text.toString()
            var password = inputPassword.editText?.text.toString()

            if(username.isEmpty()){
                inputUsername.setError("Nama Pengguna atau Email Harus Diisi")
                checkLogin = false
            }

            if(password.isEmpty()){
                inputPassword.setError("Kata Sandi Harus Diisi")
                checkLogin = false
            }
            val db by lazy { UserDB(this) }

            login()

            if(username == user && password == pass) checkLogin = true

            if(!checkLogin) return@OnClickListener

        })
    }

    private fun login(){

        val user = User(
            0,
            binding?.username?.getEditText()?.getText().toString(),
            binding?.password?.getEditText()?.getText().toString(),
            "","",""
        )
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.POST, UserApi.LOGIN_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                Log.d("volleyerr",response.toString())
                //val user = gson.fromJson(jsonObject.getJSONArray("data")[0].toString(), User::class.java)

                if(user != null)
                    Toast.makeText(this, "Login Sucess", Toast.LENGTH_SHORT).show()

                val sp = this.getSharedPreferences("user", 0)
                val editor = sp.edit()
                editor.putInt("id", jsonObject.getJSONObject("user").getInt("id"))
                editor.apply()

                val moveHome = Intent(this@LoginActivity, HomeActivity::class.java)
                    .putExtra("username", user.username)
                    .putExtra("password", user.password)
                startActivity(moveHome)

                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                Log.d("volleyerr",error.toString())
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this, errors.getString("message"), Toast.LENGTH_SHORT).show()
                    Log.d("volleyerr",errors.getString("message"))
                }
                catch (e:Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    Log.d("volleyerr",e.message.toString())
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>{
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }

            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = binding?.username?.getEditText()?.getText().toString()
                params["password"] = binding?.password?.getEditText()?.getText().toString()
                return params
            }
        }
        queue!!.add(stringRequest)
    }

    private fun setLoading(isLoading: Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            binding?.layoutLoading?.root?.visibility = View.VISIBLE
        }
        else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            binding?.layoutLoading?.root?.visibility = View.INVISIBLE
        }
    }
}
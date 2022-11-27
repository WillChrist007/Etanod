package com.william.etanodv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import com.william.etanodv2.adapters.UserAdapter
import com.william.etanodv2.api.UserApi
import com.william.etanodv2.models.Fundraising
import com.william.etanodv2.models.User1
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class UserActivity : AppCompatActivity() {
    private var srUser: SwipeRefreshLayout? = null
    private var adapter: UserAdapter? = null
    private var svUser: SearchView? = null
    private  var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srUser = findViewById(R.id.sr_user)
        svUser = findViewById(R.id.sv_user)

        srUser?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allUser() })
        svUser?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String): Boolean{
                return false
            }

            override fun onQueryTextChange(s: String): Boolean{
                adapter!!.filter.filter(s)
                return false
            }
        })

        val rvProduk = findViewById<RecyclerView>(R.id.rv_user)
        adapter = UserAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allUser()
    }

    private fun allUser() {
        srUser!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, UserApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                var user: Array<User1> = gson.fromJson(jsonObject.getJSONArray("data").toString(), Array<User1>::class.java)

                adapter!!.setUserList(user)
                adapter!!.filter.filter(svUser!!.query)
                srUser!!.isRefreshing = false

                if (!user.isEmpty())
                    FancyToast.makeText(this@UserActivity, "Data Berhasil Diambil !", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, R.drawable.etanod, false).show()
                else
                    FancyToast.makeText(this@UserActivity, "Data Kosong !", FancyToast.LENGTH_LONG, FancyToast.WARNING, R.drawable.etanod, false).show()
            }, Response.ErrorListener { error ->
                srUser!!.isRefreshing = false
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@UserActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@UserActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            // Menambahkan header pada request
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    fun deleteUser(id:Long) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(
                Method.DELETE,
                UserApi.DELETE_URL + id,
                Response.Listener { response ->
                    setLoading(false)

                    val gson = Gson()
                    var user = gson.fromJson(response, User1::class.java)
                    if (user != null)
                        Toast.makeText(
                            this@UserActivity,
                            "Data Berhasil Dihapus",
                            Toast.LENGTH_SHORT
                        )
                    allUser()
                },
                Response.ErrorListener { error ->
                    setLoading(false)
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@UserActivity,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(this@UserActivity, e.message, Toast.LENGTH_SHORT)
                    }
                }) {
            // Menambahkan header pada request
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allUser()
    }

    // Fungsi ini digunakan menampilkan layout loading
    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }
}
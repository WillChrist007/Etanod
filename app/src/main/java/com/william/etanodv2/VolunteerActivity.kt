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
import com.william.etanodv2.adapters.VolunteerAdapter
import com.william.etanodv2.api.VolunteerApi
import com.william.etanodv2.models.Volunteer
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class VolunteerActivity : AppCompatActivity() {
    private var srVolunteer: SwipeRefreshLayout? = null
    private var adapter: VolunteerAdapter? = null
    private var svVolunteer: SearchView? = null
    private  var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer)

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srVolunteer = findViewById(R.id.sr_volunteer)
        svVolunteer = findViewById(R.id.sv_volunteer)

        srVolunteer?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allVolunteer() })
        svVolunteer?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String): Boolean{
                return false
            }

            override fun onQueryTextChange(s: String): Boolean{
                adapter!!.filter.filter(s)
                return false
            }
        })

        val rvProduk = findViewById<RecyclerView>(R.id.rv_volunteer)
        adapter = VolunteerAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allVolunteer()
    }

    private fun allVolunteer() {
        srVolunteer!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, VolunteerApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                var volunteer: Array<Volunteer> = gson.fromJson(jsonObject.getJSONArray("data").toString(), Array<Volunteer>::class.java)

                adapter!!.setVolunteerList(volunteer)
                adapter!!.filter.filter(svVolunteer!!.query)
                srVolunteer!!.isRefreshing = false

                if (!volunteer.isEmpty())
                    FancyToast.makeText(this@VolunteerActivity, "Data Berhasil Diambil !", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, R.drawable.etanod, false).show()
                else
                    FancyToast.makeText(this@VolunteerActivity, "Data Kosong !", FancyToast.LENGTH_LONG, FancyToast.WARNING, R.drawable.etanod, false).show()
            }, Response.ErrorListener { error ->
                srVolunteer!!.isRefreshing = false
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@VolunteerActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@VolunteerActivity, e.message, Toast.LENGTH_SHORT).show()
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
        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allVolunteer()
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
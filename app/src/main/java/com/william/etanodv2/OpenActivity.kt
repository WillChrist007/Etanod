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
import com.william.etanodv2.adapters.OpenAdapter
import com.william.etanodv2.api.VolunteerApi
import com.william.etanodv2.models.Volunteer
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class OpenActivity : AppCompatActivity() {
    private var srOpen: SwipeRefreshLayout? = null
    private var adapter: OpenAdapter? = null
    private var svOpen: SearchView? = null
    private  var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open)

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srOpen = findViewById(R.id.sr_open)
        svOpen = findViewById(R.id.sv_open)

        srOpen?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allOpen() })
        svOpen?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String): Boolean{
                return false
            }

            override fun onQueryTextChange(s: String): Boolean{
                adapter!!.filter.filter(s)
                return false
            }
        })

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener{
            val i = Intent(this@OpenActivity, AddEditVolunteerActivity::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_open)
        adapter = OpenAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allOpen()
    }

    private fun allOpen() {
        srOpen!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, VolunteerApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                var open: Array<Volunteer> = gson.fromJson(jsonObject.getJSONArray("data").toString(), Array<Volunteer>::class.java)

                adapter!!.setOpenList(open)
                adapter!!.filter.filter(svOpen!!.query)
                srOpen!!.isRefreshing = false

                if (!open.isEmpty())
                    FancyToast.makeText(this@OpenActivity, "Data Berhasil Diambil !", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, R.drawable.etanod, false).show()
                else
                    FancyToast.makeText(this@OpenActivity, "Data Kosong !", FancyToast.LENGTH_LONG, FancyToast.WARNING, R.drawable.etanod, false).show()
            }, Response.ErrorListener { error ->
                srOpen!!.isRefreshing = false
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@OpenActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@OpenActivity, e.message, Toast.LENGTH_SHORT).show()
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

    fun deleteOpen(id:Long) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(
                Method.DELETE,
                VolunteerApi.DELETE_URL + id,
                Response.Listener { response ->
                    setLoading(false)

                    val gson = Gson()
                    var open = gson.fromJson(response, Volunteer::class.java)
                    if (open != null)
                        Toast.makeText(
                            this@OpenActivity,
                            "Data Berhasil Dihapus",
                            Toast.LENGTH_SHORT
                        )
                    allOpen()
                },
                Response.ErrorListener { error ->
                    setLoading(false)
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@OpenActivity,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(this@OpenActivity, e.message, Toast.LENGTH_SHORT)
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
        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allOpen()
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
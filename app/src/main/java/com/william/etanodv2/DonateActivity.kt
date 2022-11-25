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
import com.william.etanodv2.adapters.DonateAdapter
import com.william.etanodv2.api.FundraisingApi
import com.william.etanodv2.models.Fundraising
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class DonateActivity : AppCompatActivity() {
    private var srDonate: SwipeRefreshLayout? = null
    private var adapter: DonateAdapter? = null
    private var svDonate: SearchView? = null
    private  var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_LOCATION_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate)

        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srDonate = findViewById(R.id.sr_donate)
        svDonate = findViewById(R.id.sv_donate)

        srDonate?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allDonate() })
        svDonate?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String): Boolean{
                return false
            }

            override fun onQueryTextChange(s: String): Boolean{
                adapter!!.filter.filter(s)
                return false
            }
        })

        val fabMap = findViewById<FloatingActionButton>(R.id.fab_map)
        fabMap.setOnClickListener{
            val i = Intent(this@DonateActivity, LocationActivity::class.java)
            startActivityForResult(i, LAUNCH_LOCATION_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_donate)
        adapter = DonateAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allDonate()
    }

    private fun allDonate() {
        srDonate!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, FundraisingApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                var donate: Array<Fundraising> = gson.fromJson(response, Array<Fundraising>::class.java)

                adapter!!.setDonateList(donate)
                adapter!!.filter.filter(svDonate!!.query)
                srDonate!!.isRefreshing = false

                if (!donate.isEmpty())
                    Toast.makeText(this@DonateActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@DonateActivity, "Data Kosong!", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                srDonate!!.isRefreshing = false
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@DonateActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@DonateActivity, e.message, Toast.LENGTH_SHORT).show()
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
        if(requestCode == LAUNCH_LOCATION_ACTIVITY && resultCode == RESULT_OK) allDonate()
    }
}
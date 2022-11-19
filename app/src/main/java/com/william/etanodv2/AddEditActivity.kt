package com.william.etanodv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.william.etanodv2.api.FundraisingApi
import com.william.etanodv2.models.Fundraising
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddEditActivity : AppCompatActivity() {

    companion object{
        private val LOKASI_LIST = arrayOf("Sumatera", "Jawa", "Kalimantan", "Sulawesi", "Bali", "Papua")
    }

    private var etJudul: EditText? = null
    private var etDana: EditText? = null
    private var edLokasi: AutoCompleteTextView? = null
    private var etDurasi: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        queue = Volley.newRequestQueue(this)
        etJudul = findViewById(R.id.et_judul)
        etDana = findViewById(R.id.et_dana)
        edLokasi = findViewById(R.id.ed_lokasi)
        etDurasi = findViewById(R.id.et_durasi)
        layoutLoading = findViewById(R.id.layout_loading)

        setExposedDropDownMenu()

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener {finish()}
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_tittle)
        val id = intent.getLongExtra("id", -1)
        if(id==-1L) {
            tvTitle.setText("Tambah Fundraising")
            btnSave.setOnClickListener { createFundraising() }
        } else {
            tvTitle.setText("Edit Fundraising")
            getFundraisingById(id)

            btnSave.setOnClickListener {updateFundraising(id)}
        }

    }

    fun setExposedDropDownMenu(){
        val adapterLokasi: ArrayAdapter<String> = ArrayAdapter<String>(this,
            R.layout.item_list, LOKASI_LIST)
        edLokasi!!.setAdapter(adapterLokasi)
    }

    private fun getFundraisingById(id: Long) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, FundraisingApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val fundraising = gson.fromJson(response, Fundraising::class.java)

                etJudul!!.setText(fundraising.judul)
                etDana!!.setText(fundraising.dana)
                edLokasi!!.setText(fundraising.lokasi)
                etDurasi!!.setText(fundraising.durasi)
                setExposedDropDownMenu()

                Toast.makeText(this@AddEditActivity, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)

                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT).show()
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

    private fun createFundraising(){
        setLoading(true)

        val fundraising = Fundraising(
            etJudul!!.text.toString(),
            etDana!!.text.toString(),
            edLokasi!!.text.toString(),
            etDurasi!!.text.toString()

        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, FundraisingApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                var fundraising = gson.fromJson(response, Fundraising::class.java)

                if(fundraising != null)
                    Toast.makeText(this@AddEditActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

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
                        this@AddEditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(fundraising)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }

    private fun updateFundraising(id: Long) {
        setLoading(true)

        val fundraising = Fundraising(
            etJudul!!.text.toString(),
            etDana!!.text.toString(),
            edLokasi!!.text.toString(),
            etDurasi!!.text.toString()
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, FundraisingApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()

                var fundraising = gson.fromJson(response, Fundraising::class.java)

                if(fundraising != null)
                    Toast.makeText(this@AddEditActivity, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()

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
                        this@AddEditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val gson = Gson()
                val requestBody = gson.toJson(fundraising)
                return requestBody.toByteArray(StandardCharsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
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
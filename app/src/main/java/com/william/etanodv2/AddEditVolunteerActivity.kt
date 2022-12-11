package com.william.etanodv2

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.shashank.sony.fancytoastlib.FancyToast
import com.william.etanodv2.api.VolunteerApi
import com.william.etanodv2.databinding.ActivityAddEditVolunteerBinding
import com.william.etanodv2.models.Volunteer
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class AddEditVolunteerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditVolunteerBinding

    var kalender = Calendar.getInstance()

    companion object{
        private val LOKASI_LIST = arrayOf("Sumatera", "Jawa", "Kalimantan", "Sulawesi", "Bali", "Papua")
    }

    private var etJudul: EditText? = null
    private var etDeskripsi: EditText? = null
    private var edLokasi: AutoCompleteTextView? = null
    private var etWaktu: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_volunteer)

        binding = ActivityAddEditVolunteerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        queue = Volley.newRequestQueue(this)
        etJudul = findViewById(R.id.et_judul)
        etDeskripsi = findViewById(R.id.et_deskripsi)
        edLokasi = findViewById(R.id.ed_lokasi)
        etWaktu = findViewById(R.id.et_waktu)
        layoutLoading = findViewById(R.id.layout_loading)

        setExposedDropDownMenu()

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener {finish()}

        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_tittle)
        val id = intent.getLongExtra("id", -1)

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                kalender.set(Calendar.YEAR, year)
                kalender.set(Calendar.MONTH, monthOfYear)
                kalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateEditText()
            }
        }

        binding?.etWaktu?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View) {
                DatePickerDialog(this@AddEditVolunteerActivity,
                    dateSetListener,
                    kalender.get(Calendar.YEAR),
                    kalender.get(Calendar.MONTH),
                    kalender.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        if(id==-1L) {
            tvTitle.setText("Tambah Volunteer")

            btnSave.setOnClickListener(View.OnClickListener {
                var checkAdd = false

                createOpen()

                val judul = etJudul!!.text.toString()
                val deskripsi = etDeskripsi!!.text.toString()
                val lokasi = edLokasi!!.text.toString()
                val waktu = etWaktu!!.text.toString()

                if(!judul.isEmpty() && !deskripsi.isEmpty() && !lokasi.isEmpty() && !waktu.isEmpty()) {
                    checkAdd = true
                }

                if(!checkAdd)return@OnClickListener

                createPdf(judul, deskripsi, lokasi, waktu)

                startActivity(Intent(this@AddEditVolunteerActivity , OpenActivity::class.java))
            })
        } else {
            tvTitle.setText("Edit Volunteer")
            getOpenById(id)

            btnSave.setOnClickListener(View.OnClickListener {
                var checkEdit = false

                updateOpen(id)

                val judul = etJudul!!.text.toString()
                val deskripsi = etDeskripsi!!.text.toString()
                val lokasi = edLokasi!!.text.toString()
                val waktu = etWaktu!!.text.toString()

                if(!judul.isEmpty() && !deskripsi.isEmpty() && !lokasi.isEmpty() && !waktu.isEmpty()) {
                    checkEdit = true
                }

                if(!checkEdit)return@OnClickListener

                createPdf(judul, deskripsi, lokasi, waktu)

                startActivity(Intent(this@AddEditVolunteerActivity , OpenActivity::class.java))
            })
        }

    }

    fun setExposedDropDownMenu(){
        val adapterLokasi: ArrayAdapter<String> = ArrayAdapter<String>(this,
            R.layout.item_list, LOKASI_LIST)
        edLokasi!!.setAdapter(adapterLokasi)
    }

    private fun getOpenById(id: Long) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, VolunteerApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()

                val jsonObject = JSONObject(response)

                val open = gson.fromJson(jsonObject.getJSONObject("data").toString(), Volunteer::class.java)

                etJudul!!.setText(open.judul)
                etDeskripsi!!.setText(open.deskripsi)
                edLokasi!!.setText(open.lokasi)
                etWaktu!!.setText(open.waktu)
                setExposedDropDownMenu()

                Toast.makeText(this@AddEditVolunteerActivity, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)

                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditVolunteerActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@AddEditVolunteerActivity, e.message, Toast.LENGTH_SHORT).show()
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

    private fun createOpen(){
        setLoading(true)

        val open = Volunteer(
            etJudul!!.text.toString(),
            etDeskripsi!!.text.toString(),
            edLokasi!!.text.toString(),
            etWaktu!!.text.toString()

        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, VolunteerApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                val respond = gson.fromJson(response, Volunteer::class.java)

                if(open != null)
                    Toast.makeText(this@AddEditVolunteerActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

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
                        this@AddEditVolunteerActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@AddEditVolunteerActivity, e.message, Toast.LENGTH_SHORT).show()
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
                    params["judul"] = open.judul
                    params["deskripsi"] = open.deskripsi
                    params["lokasi"] = open.lokasi
                    params["waktu"] = open.waktu
                    return params
                }
            }
        queue!!.add(stringRequest)
    }

    private fun updateOpen(id: Long) {
        setLoading(true)

        val open = Volunteer(
            etJudul!!.text.toString(),
            etDeskripsi!!.text.toString(),
            edLokasi!!.text.toString(),
            etWaktu!!.text.toString()
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, VolunteerApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()

                var open = gson.fromJson(response, Volunteer::class.java)

                if(open != null)
                    Toast.makeText(this@AddEditVolunteerActivity, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()

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
                        this@AddEditVolunteerActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@AddEditVolunteerActivity, e.message, Toast.LENGTH_SHORT).show()
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
                params["judul"] = open.judul
                params["deskripsi"] = open.deskripsi
                params["lokasi"] = open.lokasi
                params["waktu"] = open.waktu
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

    private fun createPdf(judul: String, deskripsi: String, lokasi: String, waktu: String){
        //ini berguna untuk akses Writing ke storage HP dalam mode Download
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(pdfPath, "BUKTI MENGADAKAN KEGIATAN VOLUNTEER " +judul + ".pdf")
        FileOutputStream(file)

        //inisisalisasi pembuatan PDF
        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)
        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(5f, 5f, 5f, 5f)
        @SuppressLint("UseCompatLoadingForDrawables") val d = getDrawable(R.drawable.banner)

        //penambahan gambar pada Gambar atas
        val bitmap = (d as BitmapDrawable?)!!.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,stream)
        val bitmapData = stream.toByteArray()
        val imageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)
        val namapengguna = Paragraph("Terima Kasih Telah Mengadakan Kegiatan Volunteer").setBold().setFontSize(24f)
            .setTextAlignment(TextAlignment.CENTER)
        val group = Paragraph(
            """
                        Berikut Adalah
                        Kegiatan Volunteer yang Telah Didaftarkan
                        """.trimIndent()).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)

        //proses pembuatan table
        val width = floatArrayOf(100f, 100f)
        val table = Table(width)
        //pengisian table dengan data-data
        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
        table.addCell(Cell().add(Paragraph("Judul")))
        table.addCell(Cell().add(Paragraph(judul)))
        table.addCell(Cell().add(Paragraph("Deskripsi")))
        table.addCell(Cell().add(Paragraph(deskripsi)))
        table.addCell(Cell().add(Paragraph("Lokasi")))
        table.addCell(Cell().add(Paragraph(lokasi)))
        table.addCell(Cell().add(Paragraph("Waktu Pelaksanaan")))
        table.addCell(Cell().add(Paragraph(waktu)))
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        table.addCell(Cell().add(Paragraph("Tanggal Donasi")))
        table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
        table.addCell(Cell().add(Paragraph("Waktu Donasi")))
        table.addCell(Cell().add(Paragraph(LocalTime.now().format(timeFormatter))))

        val barcodeQRCode = BarcodeQRCode(
            """
                                        $judul
                                        $deskripsi
                                        $lokasi
                                        $waktu
                                        ${LocalDate.now().format(dateTimeFormatter)}
                                        ${LocalTime.now().format(timeFormatter)}
                                        """.trimIndent())
        val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
        val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(
            HorizontalAlignment.CENTER)

        document.add(image)
        document.add(namapengguna)
        document.add(group)
        document.add(table)
        document.add(qrCodeImage)

        document.close()
        FancyToast.makeText(this, "PDF Created !", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, R.drawable.etanod, false).show()
    }

    private fun updateEditText(){
        var temp : String
        val dateFormat = "MM/dd/yyyy"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.US)
        temp = simpleDateFormat.format(kalender.getTime())
        binding?.layoutWaktu?.getEditText()?.setText(temp)
    }
}
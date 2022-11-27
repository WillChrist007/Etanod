package com.william.etanodv2

import android.annotation.SuppressLint
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
import com.william.etanodv2.api.FundraisingApi
import com.william.etanodv2.models.Fundraising
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ViewActivity : AppCompatActivity() {

    private var etJudul: EditText? = null
    private var etDana: EditText? = null
    private var edLokasi: EditText? = null
    private var etDurasi: EditText? = null
    private var etDonasi: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        queue = Volley.newRequestQueue(this)
        etJudul = findViewById(R.id.et_judul)
        etDana = findViewById(R.id.et_dana)
        edLokasi = findViewById(R.id.ed_lokasi)
        etDurasi = findViewById(R.id.et_durasi)
        etDonasi = findViewById(R.id.et_donasi)
        layoutLoading = findViewById(R.id.layout_loading)


        val btnDonate = findViewById<Button>(R.id.btn_donate)
        btnDonate.setOnClickListener {
            val judul = etJudul!!.text.toString()
            val lokasi = edLokasi!!.text.toString()
            val donasi = etDonasi!!.text.toString()

            createPdf(judul, lokasi, donasi)

            finish()
        }
        val tvTitle = findViewById<TextView>(R.id.tv_tittle)
        val id = intent.getLongExtra("id", -1)
        if(id==-1L) {
            tvTitle.setText("Tambah Fundraising")
        } else {
            tvTitle.setText("Edit Fundraising")
            getFundraisingById(id)
        }

    }

    private fun getFundraisingById(id: Long) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, FundraisingApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()

                val jsonObject = JSONObject(response)

                val fundraising = gson.fromJson(jsonObject.getJSONArray("data")[0].toString(), Fundraising::class.java)

                etJudul!!.setText(fundraising.judul)
                etDana!!.setText(fundraising.dana)
                edLokasi!!.setText(fundraising.lokasi)
                etDurasi!!.setText(fundraising.durasi)

                Toast.makeText(this@ViewActivity, "Data berhasil diambil!", Toast.LENGTH_SHORT).show()
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)

                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@ViewActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@ViewActivity, e.message, Toast.LENGTH_SHORT).show()
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

    private fun createPdf(judul: String, lokasi: String, donasi: String){
        //ini berguna untuk akses Writing ke storage HP dalam mode Download
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file = File(pdfPath, "BUKTI BERDONASI " + judul + ".pdf")
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
        val namapengguna = Paragraph("Terima Kasih Telah Melakukan Donasi").setBold().setFontSize(24f)
            .setTextAlignment(TextAlignment.CENTER)
        val group = Paragraph(
            """
                        Berikut Adalah
                        Penggalangan Dana yang Telah Didonasi
                        """.trimIndent()).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)

        //proses pembuatan table
        val width = floatArrayOf(100f, 100f)
        val table = Table(width)
        //pengisian table dengan data-data
        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
        table.addCell(Cell().add(Paragraph("Judul")))
        table.addCell(Cell().add(Paragraph(judul)))
        table.addCell(Cell().add(Paragraph("Lokasi")))
        table.addCell(Cell().add(Paragraph(lokasi)))
        table.addCell(Cell().add(Paragraph("Donasi")))
        table.addCell(Cell().add(Paragraph(donasi)))
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        table.addCell(Cell().add(Paragraph("Tanggal Donasi")))
        table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
        table.addCell(Cell().add(Paragraph("Waktu Donasi")))
        table.addCell(Cell().add(Paragraph(LocalTime.now().format(timeFormatter))))

        val barcodeQRCode = BarcodeQRCode(
            """
                                        $judul
                                        $lokasi
                                        $donasi
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
}
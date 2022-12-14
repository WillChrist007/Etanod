package com.william.etanodv2

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import com.william.etanodv2.api.UserApi
import com.william.etanodv2.databinding.ActivityRegisterBinding
import com.william.etanodv2.notification.NotificationReceiver
import com.william.etanodv2.models.User
import com.william.etanodv2.room.users.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    var kalender = Calendar.getInstance()

    private lateinit var binding: ActivityRegisterBinding

    private var etUsername: EditText? = null
    private var etPassword: EditText? = null
    private var etEmail: EditText? = null
    private var etTanggal: EditText? = null
    private var etTelepon: EditText? = null
    private var layoutLoading: LinearLayout? = null

    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passwordK = "passwordKey"
    var sharedPreferencesRegister: SharedPreferences? = null

    private val CHANNEL_ID_1 = "channel_notification_01"
    private val CHANNEL_ID_2 = "channel_notification_02"
    private val notificationId1 = 101
    private val notificationId2 = 102

    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        createNotificationChannelRegister()

        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        queue = Volley.newRequestQueue(this)

        val moveLogin = Intent(this, LoginActivity::class.java)

        sharedPreferencesRegister = getSharedPreferences(myPreference, Context.MODE_PRIVATE)

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                kalender.set(Calendar.YEAR, year)
                kalender.set(Calendar.MONTH, monthOfYear)
                kalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateEditText()
            }
        }

        binding?.etTanggal?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View) {
                DatePickerDialog(this@RegisterActivity,
                    dateSetListener,
                    kalender.get(Calendar.YEAR),
                    kalender.get(Calendar.MONTH),
                    kalender.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        binding?.btnRegistration?.setOnClickListener (View.OnClickListener{
            etUsername = findViewById(R.id.et_username)
            etPassword = findViewById(R.id.et_password)
            etEmail = findViewById(R.id.et_email)
            etTanggal = findViewById(R.id.etTanggal)
            etTelepon = findViewById(R.id.et_telepon)
            layoutLoading = findViewById(R.id.layout_loading)

            var checkRegister = false
            createUser()

            var strUserName: String = binding.username.editText?.text.toString().trim()
            var strPass: String = binding.password.editText?.text.toString().trim()
            val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
            editor.putString(usernameK, strUserName)
            editor.putString(passwordK, strPass)
            editor.apply()

            sendNotificationSucessRegister()

            if(!checkRegister)return@OnClickListener
            startActivity(moveLogin)
        })
    }

    private fun createNotificationChannelRegister(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val title = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1, title, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val channel2 = NotificationChannel(CHANNEL_ID_2, title, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
            notificationManager.createNotificationChannel(channel2)
        }
    }

    private fun sendNotificationSucessRegister(){
        val intent: Intent = Intent(this, RegisterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val broadcastIntent: Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", "Terima kasih sudah Register")
        val actionIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_done_all)
            .setContentTitle("User: " + binding?.username?.editText?.text.toString() + " Registrasi Berhasil")
            .setContentText("Selamat Berdonasi Menggunakan Etanod")
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.etanod)))
            .setColor(Color.BLUE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "TOAST", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId1, builder.build())
        }
    }

    private fun updateEditText(){
        var temp : String
        val dateFormat = "MM/dd/yyyy"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.US)
        temp = simpleDateFormat.format(kalender.getTime())
        binding?.tanggalLahir?.getEditText()?.setText(temp)
    }

    private fun createUser(){
        setLoading(true)

        val user = User(
            0,
            etUsername!!.text.toString(),
            etPassword!!.text.toString(),
            etEmail!!.text.toString(),
            etTanggal!!.text.toString(),
            etTelepon!!.text.toString()
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, UserApi.REGISTER_URL, Response.Listener { response ->
                val gson = Gson()
                val respond = gson.fromJson(response, User::class.java)

                if(user != null)
                    Toast.makeText(this@RegisterActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

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
                        this@RegisterActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
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
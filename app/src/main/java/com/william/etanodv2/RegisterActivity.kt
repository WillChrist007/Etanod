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
import android.widget.DatePicker
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.william.etanodv2.databinding.ActivityRegisterBinding
import com.william.etanodv2.notification.NotificationReceiver
import com.william.etanodv2.room.users.User
import com.william.etanodv2.room.users.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    var kalender = Calendar.getInstance()

    val dbUser by lazy { UserDB(this) }

    private lateinit var binding: ActivityRegisterBinding

    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passwordK = "passwordKey"
    var sharedPreferencesRegister: SharedPreferences? = null

    private val CHANNEL_ID_1 = "channel_notification_01"
    private val CHANNEL_ID_2 = "channel_notification_02"
    private val notificationId1 = 101
    private val notificationId2 = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannelRegister()

        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
            var checkRegister = false

            val inputUsername: String = binding?.username?.getEditText()?.getText().toString()
            val inputPassword: String = binding?.password?.getEditText()?.getText().toString()
            val inputEmail: String = binding?.email?.getEditText()?.getText().toString()
            val inputTanggal: String = binding?.tanggalLahir?.getEditText()?.getText().toString()
            val inputTelepon: String = binding?.telepon?.getEditText()?.getText().toString()

            if(inputUsername.isEmpty()){
                binding?.username?.setError("Username Tidak Boleh Kosong")
                checkRegister = false
            }

            if(inputPassword.isEmpty()){
                binding?.password?.setError("Password Tidak Boleh Kosong")
                checkRegister = false
            }

            if(inputEmail.isEmpty()){
                binding?.email?.setError("Email Tidak Boleh Kosong")
                checkRegister = false
            }

            if(inputTanggal.isEmpty()){
                binding?.tanggalLahir?.setError("Tanggal Lahir Tidak Boleh Kosong")
                checkRegister = false
            }

            if(inputTelepon.isEmpty()){
                binding?.telepon?.setError("Nomor Telepon Tidak Boleh Kosong")
                checkRegister = false
            }else if(inputTelepon.length < 12){
                binding?.telepon?.setError("Nomor Telepon TIdak Valid")
                checkRegister = false
            }

            if(!inputUsername.isEmpty() && !inputPassword.isEmpty() && !inputEmail.isEmpty() && !inputTanggal.isEmpty() && !inputTelepon.isEmpty() && inputTelepon.length >= 12){
                checkRegister = true

                CoroutineScope(Dispatchers.IO).launch {
                    dbUser.userDao().addUser(
                        User(0, inputUsername, inputPassword, inputEmail, inputTanggal, inputTelepon)
                    )
                    finish()
                }

                var strUserName: String = binding.username.editText?.text.toString().trim()
                var strPass: String = binding.password.editText?.text.toString().trim()
                val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
                editor.putString(usernameK, strUserName)
                editor.putString(passwordK, strPass)
                editor.apply()

                sendNotificationSucessRegister()
            }

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

}
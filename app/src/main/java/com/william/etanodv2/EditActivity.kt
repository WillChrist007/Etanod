package com.william.etanodv2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.william.etanodv2.notification.NotificationReceiver
import com.william.etanodv2.room.Constant
import com.william.etanodv2.room.items.Item
import com.william.etanodv2.room.items.ItemDB
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    val dbItem by lazy { ItemDB(this) }
    private var itemId: Int = 0

    private val CHANNEL_ID_1 = "channel_notification_01"
    private val CHANNEL_ID_2 = "channel_notification_02"
    private val notificationId1 = 101
    private val notificationId2 = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannelFundraising()

        setContentView(R.layout.activity_edit)
        setupView()
        setupListener()


        Toast.makeText(this, itemId.toString(), Toast.LENGTH_SHORT).show()
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_CREATE -> {
                button_update.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
                getItem()
            }
            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getItem()
            }
        }
    }

    private fun setupListener() {
        button_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                dbItem.itemDao().addItem(
                    Item(0,edit_judul.text.toString(),
                        edit_jumlah.text.toString(),
                        edit_durasi.text.toString())
                )
                sendNotificationSucessFundraising()
                finish()
            }
        }
        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                dbItem.itemDao().updateItem(
                    Item(itemId,edit_judul.text.toString(),
                        edit_jumlah.text.toString(),
                        edit_durasi.text.toString())
                )
                finish()
            }
        }
    }

    fun getItem() {
        itemId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val items = dbItem.itemDao().getItem(itemId)[0]
            edit_judul.setText(items.judul)
            edit_jumlah.setText(items.jumlah)
            edit_durasi.setText(items.durasi)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun createNotificationChannelFundraising(){
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

    private fun sendNotificationSucessFundraising(){
        val intent : Intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val broadcastIntent : Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", R.id.username.toString())
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val icon: Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.etanod)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.etanod)
            .setContentTitle("Anda Berhasil Mengalang Dana!")
            .setContentText("Terima Kasih Orang Baik")
            .setLargeIcon(icon)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(getString(R.string.terimakasih))
                .setBigContentTitle("Terima Kasih")
                .setSummaryText("Penggalangan Dana " + edit_judul.text.toString()))
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(Color.RED)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "TOAST", actionIntent)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId1, builder.build())
        }
    }
}
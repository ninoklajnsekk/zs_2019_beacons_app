package si.inova.zimskasola


import android.app.*
import android.content.Intent
import android.os.IBinder
import android.os.Parcel
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.MessageListener
import com.google.android.gms.nearby.messages.Strategy
import com.google.android.gms.nearby.messages.SubscribeOptions

import android.os.Binder
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.example.zimskasola.R
import si.inova.zimskasola.activities.MainSwipeActivity





class BackgroundScanService : IntentService(BackgroundScanService::class.simpleName) {
    override fun onHandleIntent(intent: Intent?) {
        Log.d("serviceok","ok")
        val options = SubscribeOptions.Builder()
            .setStrategy(Strategy.BLE_ONLY)
            .build()
        Nearby.getMessagesClient(this).subscribe(object : MessageListener() {
            override fun onFound(p0: com.google.android.gms.nearby.messages.Message?) {
                var notificationManager: NotificationManager =
                    getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
                var parcel = Parcel.obtain()
                parcel.writeString("Zaznali smo premik na novo lokacijo")

                val resultIntent = Intent(applicationContext, MainSwipeActivity::class.java)

                val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(applicationContext).run {

                    addNextIntentWithParentStack(resultIntent)

                    getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                }




                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val name = getString(R.string.nahajas_se_v)
                    val descriptionText = getString(R.string.nahajas_se_v)
                    val importance = NotificationManager.IMPORTANCE_DEFAULT
                    val channel = NotificationChannel("1000", name, importance).apply {
                        description = descriptionText
                    }

                    notificationManager.createNotificationChannel(channel)
                }


                var not = NotificationCompat.Builder(applicationContext, "1000")
                    .setSmallIcon(com.example.zimskasola.R.drawable.notification_icon)
                    .setContentTitle("Beacons")
                    .setContentIntent(resultPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)

                with(NotificationManagerCompat.from(applicationContext)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(100, not.build())
                }


            }
        }, options);
    }

 }

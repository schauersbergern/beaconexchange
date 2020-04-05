package com.example.beaconexchange.beacon

import android.R
import android.app.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.beaconexchange.MainActivity
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter


class BeaconSenderService : Service() {

    private val CHANNEL_ID = "ForegroundService Kotlin"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("BeaconSenderService", "starting service")

        intent?.let {
            startBeaconForegroundService(it)
        }

        startAdvertising()
        return START_STICKY
    }

    private fun startAdvertising() {
        val list = mutableListOf(0L)
        val beacon = Beacon.Builder()
            .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
            .setId2("1")
            .setId3("2")
            .setManufacturer(0x0118) // Radius Networks.  Change this for other beacon layouts
            .setTxPower(-59)
            .setDataFields(list) // Remove this for beacon layouts without d: fields
            .build()

        val beaconParser = BeaconParser().setBeaconLayout(MainActivity.ALTBEACON)

        val beaconTransmitter = BeaconTransmitter(applicationContext, beaconParser)

        beaconTransmitter.startAdvertising(beacon, object : AdvertiseCallback() {
            override fun onStartFailure(errorCode: Int) {
                Log.e(
                    "TAG",
                    "Advertisement start failed with code: $errorCode"
                )
            }

            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                Log.i("TAG", "Advertisement start succeeded.")
            }
        })
    }


    private fun startBeaconForegroundService(intent: Intent) {
        val input = intent.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("App is sending Beacons")
            .setContentText(input)
            .setSmallIcon(R.drawable.star_big_on)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }


}
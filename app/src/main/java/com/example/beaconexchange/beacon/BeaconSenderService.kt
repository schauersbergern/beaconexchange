package com.example.beaconexchange.beacon

import android.app.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.beaconexchange.AlarmManager
import com.example.beaconexchange.Constants
import com.example.beaconexchange.Constants.Companion.ALTBEACON
import com.example.beaconexchange.Constants.Companion.PROTEGO_UUID
import com.example.beaconexchange.startBeaconForegroundService
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter

class BeaconSenderService : Service() {

    private lateinit var alarmManager: AlarmManager

    companion object {
        private const val CHANNEL_ID = Constants.CHANNEL_SENDER_ID
        private const val CHANNEL_TITLE = Constants.CONSTANT_NOTIFICATION_TEXT
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getStringExtra(Constants.DEVICE_ID)?.let {
            Log.i("BeaconSenderService", "starting service")
            startBeaconForegroundService(intent, CHANNEL_ID,CHANNEL_TITLE )
            startAdvertising(it)

            alarmManager = AlarmManager(applicationContext)
        }
        return START_STICKY
    }

    private fun startAdvertising(deviceId: String) {
        val list = mutableListOf(0L)
        val beacon = Beacon.Builder()
            .setId1(PROTEGO_UUID)
            .setId2(deviceId)
            .setId3(deviceId)
            .setManufacturer(0x0118) // Radius Networks.  Change this for other beacon layouts
            .setTxPower(-59)
            .setDataFields(list) // Remove this for beacon layouts without d: fields
            .build()

        val beaconParser = BeaconParser().setBeaconLayout(ALTBEACON)
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



}
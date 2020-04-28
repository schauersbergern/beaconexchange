package com.example.beaconexchange.beacon

import android.app.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.beaconexchange.Constants.Companion.ALTBEACON
import com.example.beaconexchange.startBeaconForegroundService
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter

class BeaconSenderService : Service() {

    companion object {
        private const val CHANNEL_ID = "BeaconSenderService"
        private const val CHANNEL_TITLE = "You are protected"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("BeaconSenderService", "starting service")

        intent?.let {
            startBeaconForegroundService(it, CHANNEL_ID,CHANNEL_TITLE )
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
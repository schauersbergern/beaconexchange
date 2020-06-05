package com.protego.beaconexchange.service

import android.app.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.protego.beaconexchange.Constants
import com.protego.beaconexchange.Constants.Companion.ALTBEACON
import com.protego.beaconexchange.Constants.Companion.FOREGROUND_ID
import com.protego.beaconexchange.Constants.Companion.MANUFACTURER
import com.protego.beaconexchange.Constants.Companion.PROTEGO_ID
import com.protego.beaconexchange.R
import com.protego.beaconexchange.getForegroundNotification
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter

class BeaconSenderService : Service() {

    private var beaconTransmitter : BeaconTransmitter? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (beaconTransmitter == null || (beaconTransmitter != null && !beaconTransmitter!!.isStarted) ) {
            initTransmitter()
            intent?.getStringExtra(Constants.DEVICE_ID)?.let {
                Log.i(name(), "starting service")

                startAdvertising(it)
            }
            startForeground(FOREGROUND_ID, application.getForegroundNotification(getString(R.string.bg_notification_title)))
        }
        return START_STICKY
    }

    private fun startAdvertising(deviceId: String) {
        val beacon = Beacon.Builder()
            .setId1(deviceId)
            .setId2(PROTEGO_ID)
            .setId3("0")
            .setManufacturer(MANUFACTURER)
            .setTxPower(-59)
            .setDataFields(mutableListOf(0L))
            .build()

        beaconTransmitter?.startAdvertising(beacon, object : AdvertiseCallback() {
            override fun onStartFailure(errorCode: Int) {
                Log.e(name(), "Advertisement start failed with code: $errorCode")
                restartFresh(deviceId)
            }

            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                Log.i(name(), "Advertisement start succeeded.")
            }
        })
    }

    private fun restartFresh(deviceId: String) {
        initTransmitter()
        startAdvertising(deviceId)
    }

    private fun initTransmitter() {
        beaconTransmitter = BeaconTransmitter(applicationContext, BeaconParser().setBeaconLayout(ALTBEACON))
        beaconTransmitter?.advertiseMode = AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(name(), "${name()} destroyed")
        beaconTransmitter?.stopAdvertising()
        beaconTransmitter = null
    }

    private fun name() : String {
        return javaClass.simpleName
    }

}
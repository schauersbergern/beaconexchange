package com.example.beaconexchange.service

import android.app.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.beaconexchange.Constants
import com.example.beaconexchange.Constants.Companion.ALTBEACON
import com.example.beaconexchange.Constants.Companion.PROTEGO_UUID
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter

class BeaconSenderService : Service() {

    private var beaconTransmitter : BeaconTransmitter? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initTransmitter()
        intent?.getStringExtra(Constants.DEVICE_ID)?.let {
            Log.i(name(), "starting service")

            startAdvertising(it)
        }
        return START_STICKY
    }

    private fun startAdvertising(deviceId: String) {
        val beacon = Beacon.Builder()
            .setId1(PROTEGO_UUID)
            .setId2(deviceId)
            .setId3("0")
            .setManufacturer(0x0118) // Radius Networks.  Change this for other beacon layouts
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
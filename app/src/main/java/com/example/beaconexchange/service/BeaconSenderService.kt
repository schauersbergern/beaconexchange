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

    private lateinit var beaconTransmitter : BeaconTransmitter

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getStringExtra(Constants.DEVICE_ID)?.let {
            Log.i("BeaconSenderService", "starting service")
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

        val beaconParser = BeaconParser().setBeaconLayout(ALTBEACON)
        beaconTransmitter = BeaconTransmitter(applicationContext, beaconParser)

        beaconTransmitter.startAdvertising(beacon, object : AdvertiseCallback() {
            override fun onStartFailure(errorCode: Int) {
                Log.e(
                    name(),
                    "Advertisement start failed with code: $errorCode"
                )
            }

            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                Log.i(name(), "Advertisement start succeeded.")
            }
        })
    }

    override fun onDestroy() {
        Log.i(name(), "${name()} destroyed")
        beaconTransmitter.stopAdvertising()
        super.onDestroy()
    }

    private fun name() : String {
        return javaClass.simpleName
    }

}
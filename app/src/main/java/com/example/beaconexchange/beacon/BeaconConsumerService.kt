package com.example.beaconexchange.beacon

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Parcelable
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.beaconexchange.AlarmManager
import com.example.beaconexchange.BluetoothMessage
import com.example.beaconexchange.Constants.Companion.BEACON_MESSAGE
import com.example.beaconexchange.Constants.Companion.BEACON_UPDATE
import org.altbeacon.beacon.*


class BeaconConsumerService : Service(), BeaconConsumer {

    private lateinit var beaconManager: BeaconManager
    private lateinit var alarmManager: AlarmManager

    companion object {
        private const val TAG = "BeaconConsumerService"
        private const val CHANNEL_ID = "BeaconConsumerService"
        private const val CHANNEL_TITLE = "App is receiving"
    }

    private val rangeNotifer = RangeNotifier { beacons: MutableCollection<Beacon>, region: Region ->
        Log.i(
            TAG,
            "RangeNotifier update, number of beacons in region: ${beacons.size}, region: $region"
        )
        if (beacons.isNotEmpty()) {
            val beaconContacts = beacons.map { beacon: Beacon ->
                Log.d(TAG, "RangeNotifier beacon detected: ${beacon}")

                val btAddress = beacon.bluetoothAddress
                val btName = beacon.bluetoothName ?: "No Name"
                val rssi = beacon.rssi
                val distance = beacon.distance
                val distCentimeters = (distance * 100).toInt()
                alarmManager.checkDistance(distCentimeters)

                val data = BluetoothMessage(btName, btAddress, distCentimeters, rssi)

                sendMessageToActivity(data)
            }

        }
    }

    private fun sendMessageToActivity(msg: Parcelable) {
        val intent = Intent(BEACON_UPDATE)
        intent.putExtra(BEACON_MESSAGE, msg)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "starting service")

        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.bind(this)

        alarmManager = AlarmManager(applicationContext)

        // don't need to be sticky... if killed and restarted, the MainActivity BootstrapNotifier will restart the service
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Log.i(TAG, "destroying service")
        super.onDestroy()
        beaconManager.removeRangeNotifier(rangeNotifer)
        beaconManager.unbind(this)
    }

    override fun onBeaconServiceConnect() {
        Log.i(TAG, "beaconService connected, start range scanning...")
        beaconManager.startRangingBeaconsInRegion(Region("BeaconExchangeScanningRegion", null, null, null))
        beaconManager.addRangeNotifier(rangeNotifer)

    }

}

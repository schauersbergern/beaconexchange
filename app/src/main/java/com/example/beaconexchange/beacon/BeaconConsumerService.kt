package com.example.beaconexchange.beacon

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.beaconexchange.Constants.Companion.BEACON_MESSAGE
import com.example.beaconexchange.Constants.Companion.BEACON_UPDATE
import org.altbeacon.beacon.*

class BeaconServiceBinder(consumerService: BeaconConsumerService) : Binder() {
    var service: BeaconConsumerService = consumerService
        get() = field
}

class BeaconConsumerService : Service(), BeaconConsumer {

    private lateinit var beaconManager: BeaconManager

    companion object {
        private const val TAG = "BeaconConsumerService"
    }

    private val rangeNotifer = RangeNotifier { beacons: MutableCollection<Beacon>, region: Region ->
        Log.i(
            TAG,
            "RangeNotifier update, number of beacons in region: ${beacons.size}, region: $region"
        )
        if (beacons.isNotEmpty()) {
            val beaconContacts = beacons.map { beacon: Beacon ->
                Log.d(TAG, "RangeNotifier beacon detected: ${beacon}")

                val firstBeacon = beacons.iterator().next()
                val btAddress = firstBeacon.bluetoothAddress
                val btName = firstBeacon.bluetoothName
                val rssi = firstBeacon.rssi
                val distance = firstBeacon.distance
                val data = "The beacon with Address $btAddress has the name $btName is about $distance meters away and has rssi of $rssi"

                sendMessageToActivity(data)
            }

        }
    }

    private fun sendMessageToActivity(msg: String) {
        val intent = Intent(BEACON_UPDATE)
        intent.putExtra(BEACON_MESSAGE, msg)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return BeaconServiceBinder(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "starting service")

        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.bind(this)

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

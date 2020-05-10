package com.example.beaconexchange

import android.app.Activity
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.beaconexchange.beacon.BeaconSenderService
import com.example.intro.presentation.IntroActivity
import org.altbeacon.beacon.*


class MainActivity : AppCompatActivity(), BeaconConsumer {

    private lateinit var beaconManager: BeaconManager
    private lateinit var alarmManager: AlarmManager

    private var serviceRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alarmManager = AlarmManager(this)

        if (shouldShowOnboarding()) {
            startIntro()
            return
        }

        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        beaconManager.unbind(this)
    }

    private fun init() {
        setContentView(R.layout.activity_main)
        verifyBluetooth()

        if (!bluetoothLeIsAvailable()) {
            Toast.makeText(this, "Blueooth is not supported", Toast.LENGTH_SHORT).show()
        } else {
            beaconManager = BeaconManager.getInstanceForApplication(this)
            beaconManager.bind(this)
        }
    }

    private fun startIntro() {
        Intent(this, IntroActivity::class.java).apply {
            startActivityForResult(this, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            getPreferences(Context.MODE_PRIVATE)?.edit()?.putBoolean("SHOWOB", false)?.commit()
            init()
        }
    }

    private fun shouldShowOnboarding(): Boolean {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean("SHOWOB", true)
    }

    private fun bluetoothLeIsAvailable(): Boolean {
        val man = applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = man.adapter
        return adapter != null
    }

    private fun verifyBluetooth() {
        try {
            if (!beaconManager.checkAvailability()) {
                alertDialog(
                    getString(R.string.not_enabled),
                    getString(R.string.prompt_enable)
                ).show()
            }
        } catch (e: RuntimeException) {
            alertDialog(
                getString(R.string.not_available),
                getString(R.string.no_ble_support)
            ).show()
        }
    }


    fun startServices(deviceId: String) {
        beaconManager.unbind(this)
        startService(getSenderServiceIntent(deviceId))
        (application as BeaconExchangeApplication).startBeaconForegroundService()
        beaconManager.bind(this)
        serviceRunning = true
    }

    fun stopServices() {
        beaconManager.unbind(this)
        stopService(Intent(this, BeaconSenderService::class.java))
        (application as BeaconExchangeApplication).stopBeaconForegroundService()
        beaconManager.bind(this)
        serviceRunning = false
    }

    private fun getSenderServiceIntent(deviceId: String): Intent {
        return Intent(this, BeaconSenderService::class.java).apply {
            putExtra(Constants.DEVICE_ID, deviceId)
        }
    }

    private fun sendMessageToFragment(msg: Parcelable) {
        val intent = Intent(Constants.BEACON_UPDATE)
        intent.putExtra(Constants.BEACON_MESSAGE, msg)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    override fun onBeaconServiceConnect() {

        beaconManager.removeAllRangeNotifiers()
        beaconManager.addRangeNotifier { beacons, _ ->
            beacons.map { beacon: Beacon ->
                Log.d(name(), "RangeNotifier beacon detected: $beacon")

                if (beacon.isProtego() && serviceRunning) {
                    val data = beacon.getBluetoothMessage()
                    alarmManager.checkDistance(data.distCentimeters)
                    sendMessageToFragment(data)
                }
            }
        }

        beaconManager.startRangingBeaconsInRegion(RegionFactory.getRegion())
    }

    private fun name(): String {
        return javaClass.simpleName
    }
}

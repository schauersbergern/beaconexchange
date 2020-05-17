package com.example.beaconexchange

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.os.PowerManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import com.example.beaconexchange.Constants.Companion.ONBOARDING_KEY
import com.example.beaconexchange.Constants.Companion.ONBOARDING_REQUEST
import com.example.beaconexchange.service.BeaconSenderService
import com.example.beaconexchange.ui.settings.SettingsViewModel
import com.example.intro.presentation.IntroActivity
import com.example.localdatasource.LocalDatabase
import com.example.localdatasource.SettingsRepository
import com.example.localdatasource.entities.Settings
import kotlinx.coroutines.*
import org.altbeacon.beacon.*

class MainActivity : AppCompatActivity(), BeaconConsumer {

    private lateinit var beaconManager: BeaconManager
    lateinit var alarmManager: AlarmManager

    private lateinit var viewModel : SettingsViewModel

    private var serviceRunning = false

    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alarmManager = AlarmManager(this)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

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
        beaconManager = BeaconManager.getInstanceForApplication(this)
        //TODO: Deactivate UI if Bluetooth not available
        verifyBluetooth()
        beaconManager.bind(this)

        viewModel.settings.observe(this, Observer {
            if (it == null) {
                alarmManager.changeSettings(getStandardSettings())
            } else {
                alarmManager.changeSettings(it)
            }
        })
    }

    private fun startIntro() {
        Intent(this, IntroActivity::class.java).apply {
            startActivityForResult(this, ONBOARDING_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ONBOARDING_REQUEST && resultCode == Activity.RESULT_OK) {
            getPreferences(Context.MODE_PRIVATE)?.edit()?.putBoolean(ONBOARDING_KEY, false)?.apply()
            init()
        }
    }

    private fun shouldShowOnboarding(): Boolean {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(ONBOARDING_KEY, true)
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
        if (!serviceRunning){
            wakeLock = getWakeLock()
            beaconManager.unbind(this)
            startService(getSenderServiceIntent(deviceId))
            (application as BeaconExchangeApplication).startBeaconForegroundService()
            beaconManager.bind(this)
            serviceRunning = true
        }
    }

    fun stopServices() {
        if (serviceRunning) {
            wakeLock?.release()
            beaconManager.unbind(this)
            stopService(Intent(this, BeaconSenderService::class.java))
            (application as BeaconExchangeApplication).stopBeaconForegroundService()
            beaconManager.bind(this)
            serviceRunning = false
        }
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
                    alarmManager.checkRssiDistance(data.rssi)
                    sendMessageToFragment(data)
                }
            }
        }

        beaconManager.startRangingBeaconsInRegion(RegionFactory.getRegion())
    }

    override fun onBackPressed() {
        val dest = findNavController(R.id.nav_host_fragment).currentDestination
        if (dest?.id == R.id.navigation_start) {
            moveTaskToBack(false)
        } else {
            super.onBackPressed()
        }
    }

    private fun name(): String {
        return javaClass.simpleName
    }
}

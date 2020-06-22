package com.protego.beaconexchange

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import com.protego.beaconexchange.Constants.Companion.ONBOARDING_KEY
import com.protego.beaconexchange.Constants.Companion.ONBOARDING_REQUEST
import com.protego.beaconexchange.service.BeaconSenderService
import com.protego.beaconexchange.ui.settings.SettingsViewModel
import com.protego.beaconexchange.ui.excluded.ExcludedViewModel
import com.protego.intro.presentation.IntroActivity
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import timber.log.Timber


class MainActivity : AppCompatActivity(), BeaconConsumer {

    private lateinit var beaconManager: BeaconManager
    private lateinit var settingsViewModel : SettingsViewModel
    private lateinit var excludedViewModel : ExcludedViewModel
    private lateinit var log: Timber.Tree
    lateinit var alarmManager: AlarmManager

    private var serviceRunning = false
    private var wakeLock: PowerManager.WakeLock? = null

    private val timestamp: String
        get() {return (System.currentTimeMillis() / 1000).toString()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alarmManager = AlarmManager(this)
        beaconManager = BeaconManager.getInstanceForApplication(this)
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        excludedViewModel = ViewModelProvider(this).get(ExcludedViewModel::class.java)

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

        log = initLogger(this)

        settingsViewModel.settings.observe(this, Observer {
            if (it == null) {
                alarmManager.changeSettings(getStandardSettings(this))
            } else {
                alarmManager.changeSettings(it)
            }
        })

        excludedViewModel.excludedLive.observe(this, Observer { excluded ->
            if (excluded != null) {
                excludedViewModel.addExcluded(excluded)
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


    fun startServices(deviceId: String) {
        if (!serviceRunning){
            "start".log()
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
            "stop".log()
            wakeLock?.release()
            beaconManager.unbind(this)
            stopService(Intent(this, BeaconSenderService::class.java))
            (application as BeaconExchangeApplication).stopBeaconForegroundService()
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

                var send = true
                if (excludedViewModel.isInExcluded(beacon.id1.toString())) {
                    send = false
                }

                if (beacon.isProtego() && serviceRunning && send) {
                    val data = beacon.getBluetoothMessage()
                    alarmManager.checkRssiDistance(data.rssi)
                    sendMessageToFragment(data)
                    "${data.deviceId}, ${data.rssi}".log()
                }
            }
        }
        beaconManager.startRangingBeaconsInRegion(RegionFactory.getRegion())
    }

    fun addToExcluded(deviceId: String) {
        excludedViewModel.addToExcluded(deviceId)
        Toast.makeText(
            this,
            getString(R.string.add_device_to_excluded).replace("%s", deviceId),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onBackPressed() {
        val dest = findNavController(R.id.nav_host_fragment).currentDestination
        if (dest?.id == R.id.navigation_monitoring) {
            moveTaskToBack(false)
        } else {
            super.onBackPressed()
        }
    }

    private fun String.log() {
        if (settingsViewModel.isLoggingEnabled()) {
            log.i(", $timestamp, $this")
        }
    }
}

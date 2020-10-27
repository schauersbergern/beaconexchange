package com.protego.beaconexchange

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.os.PowerManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import com.protego.beaconexchange.bluetooth.BluetoothService
import com.protego.beaconexchange.helper.*
import com.protego.beaconexchange.ui.settings.SettingsViewModel
import com.protego.beaconexchange.ui.excluded.ExcludedViewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var settingsViewModel : SettingsViewModel
    private lateinit var log: Timber.Tree
    lateinit var alarmManager: AlarmManager

    private var serviceRunning = false
    private var wakeLock: PowerManager.WakeLock? = null

    private val timestamp: String
        get() {return (System.currentTimeMillis() / 1000).toString()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmManager = AlarmManager(this)
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        log = initLogger(this)

        settingsViewModel.settings.observe(this, Observer {
            if (it == null) {
                alarmManager.changeSettings(getStandardSettings(this))
            } else {
                alarmManager.changeSettings(it)
            }
        })

       registerReceiver(bluetoothOffReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(bluetoothOffReceiver)
    }

    fun startServices(deviceId: String) {
        if (!serviceRunning){
            "start".log()
            wakeLock = getWakeLock()
            startService(getSenderServiceIntent(deviceId))
            //BluetoothService.start(this)
            serviceRunning = true
        }
    }

    fun stopServices() {
        if (serviceRunning) {
            "stop".log()
            wakeLock?.release()
            stopService(Intent(this, BluetoothService::class.java))
            //BluetoothService.stop()
            serviceRunning = false
        }
    }

    private fun getSenderServiceIntent(deviceId: String): Intent {
        return Intent(this, BluetoothService::class.java).apply {
            putExtra(Constants.DEVICE_ID, deviceId)
        }
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

    private val bluetoothOffReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if(intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {
                    if (serviceRunning) {
                        stopServices()
                        context.showNotification(
                            getString(R.string.bluetooth_turned_off),
                            getString(R.string.bluetooth_turn_on)
                        )
                    }
                }
            }
        }
    }
}

package com.protego.beaconexchange.helper

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.media.RingtoneManager
import android.util.Log
import com.protego.beaconexchange.helper.Constants.Companion.LOG_DIR
import com.protego.beaconexchange.helper.Constants.Companion.LOG_FILE
import com.protego.beaconexchange.R
import com.protego.localdatasource.entities.Settings
import fr.bipi.tressence.file.FileLoggerTree
import timber.log.Timber
import java.util.*
import kotlin.concurrent.schedule

fun getDefaultRingtone() : String {
    return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
}

fun getStandardSettings(context: Context) : Settings {
    return Settings(
        0,
        context.resources.getInteger(R.integer.rssi_value_max_range),
        getDefaultRingtone(),
        isAlarmActive = false,
        isVibrationActive = true,
        loggingEnabled = false)
}

fun randomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun initLogger(ctx : Context) = FileLoggerTree.Builder()
        .withFileName(LOG_FILE)
        .withDirName(LOG_DIR)
        .withSizeLimit(20000)
        .withFileLimit(20)
        .withMinPriority(Log.DEBUG)
        .appendToFile(true)
        .withContext(ctx)
        .build().also {
            Timber.plant(it)
        }

fun startDummyAdvertisingForBluetoothName() {
    val advertiser: BluetoothLeAdvertiser = BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser
    val data: AdvertiseData = AdvertiseData.Builder().setIncludeDeviceName(true).build()
    val advertiseSettings = AdvertiseSettings.Builder()
        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
        .build()

    val callback: AdvertiseCallback = object : AdvertiseCallback() {

        private var timer: TimerTask? = null
        private val cb = this

        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            super.onStartSuccess(settingsInEffect)
            Log.d("Booo", "Start")

            timer = Timer("", false).schedule(20000) {
                advertiser.stopAdvertising(cb)
                Log.d("Booo", "Stop")
            }
        }
    }
    advertiser.startAdvertising(advertiseSettings, data, callback)
}
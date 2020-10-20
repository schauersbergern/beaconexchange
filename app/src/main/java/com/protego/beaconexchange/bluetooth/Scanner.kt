package com.protego.beaconexchange.bluetooth

import android.content.Context
import android.content.Intent
import android.os.ParcelUuid
import android.os.Parcelable
import android.util.Base64
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.polidea.rxandroidble2.*
import com.polidea.rxandroidble2.scan.*
import com.protego.beaconexchange.AlarmManager
import com.protego.beaconexchange.domain.BluetoothMessage
import com.protego.beaconexchange.helper.Constants
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class Scanner(private val scanner: RxBleClient,
              private val listener: ScanEventListener,
              private val alarmManager: AlarmManager,
              private val context: Context
) {

    fun scan(): Disposable {
        // From Android Nougat scanning is throttled to 5 times in 30 seconds
        return Observable.interval(6500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .doOnError { Timber.e(it, "Error in scanner loop: ${it}")}
            .filter { scanner.state == RxBleClient.State.READY}
            .flatMap {
                Timber.d("Scanning for nearby devices")
                findDevices()
            }
            .subscribe({
                listener.handle(it)
            }, {
                Timber.e(it, "Error occured: ${it}")
            })
    }

    fun findDevices() =
        scanner.scanBleDevices(scannerSettings(), workspaceServiceFilter(), iOSBackgroundFilter())
            .filter { it.bleDevice.connectionState == RxBleConnection.RxBleConnectionState.DISCONNECTED }
            .filter { it.callbackType != ScanCallbackType.CALLBACK_TYPE_MATCH_LOST}
            .take(2, TimeUnit.SECONDS)
            .flatMap {
                scanDevice(it, it.scanRecord)
            }

    fun scanDevice(device: ScanResult, scanRecord: ScanRecord) =
        device.bleDevice
            .establishConnection(false)
            .subscribeOn(Schedulers.io())
            .retryWhen {
                Timber.i("Unable to connect, retrying after a wait")
                it.delay(200, TimeUnit.MILLISECONDS)
            }
            .doOnError { Timber.e(it, "Error in connecting to device") }
            .flatMapSingle {
                Timber.d("About to read RSSI")
                it.readRssi()
            }
            // The connection can stall so if there is no response for 2 seconds, return an arbitrary out of range value.
            .timeout(2, TimeUnit.SECONDS, Observable.just(-200))
            .take(1)
            .map {
                Timber.d("RSSI: ${it} Mac: ${device.bleDevice.macAddress}, Manufacturer data: ${Base64.encodeToString(device.scanRecord.getManufacturerSpecificData(76) ?: "a".toByteArray(Charsets.US_ASCII), Base64.DEFAULT)}")

                val bla = device.scanRecord.serviceData

                val ja = bla[ParcelUuid(BluetoothService.SERVICE_ID)] as ByteArray

                val ka = ja.toString(Charsets.UTF_8)

                val data = BluetoothMessage(
                    ka,
                    device.bleDevice.name ?: "",
                    device.bleDevice.macAddress,
                    0,
                    it
                )
                alarmManager.checkRssiDistance(data.rssi)
                sendMessageToFragment(data)

                ScanEvent(device = device.bleDevice.macAddress, rssi = it, txPower = scanRecord.txPowerLevel)
            }

    private fun sendMessageToFragment(msg: Parcelable) {
        val intent = Intent(Constants.BEACON_UPDATE)
        intent.putExtra(Constants.BEACON_MESSAGE, msg)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    private fun workspaceServiceFilter(): ScanFilter =
        ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(BluetoothService.SERVICE_ID))
            .build()

    private fun iOSBackgroundFilter(): ScanFilter =
        ScanFilter.Builder().setServiceUuid(null).setManufacturerData(76,  Base64.decode("AQBAAAAAAAAAAAAAAAAAAAA=", Base64.DEFAULT)).build()

    private fun scannerSettings() =
        ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .build()

}

data class ScanEvent(val device: String, val txPower: Int, val rssi: Int, val timestamp: LocalDateTime = LocalDateTime.now())

interface ScanEventListener {
    fun handle(event: ScanEvent)
}
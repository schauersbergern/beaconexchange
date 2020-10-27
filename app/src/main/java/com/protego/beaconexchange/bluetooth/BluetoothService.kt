package com.protego.beaconexchange.bluetooth

import android.app.Service
import android.bluetooth.*
import android.bluetooth.BluetoothGattService.SERVICE_TYPE_PRIMARY
import android.bluetooth.le.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.ParcelUuid
import androidx.core.content.ContextCompat
import com.protego.beaconexchange.helper.Constants
import com.protego.beaconexchange.helper.Constants.Companion.ANDROID_MANUFACTURE_ID
import com.protego.beaconexchange.helper.Constants.Companion.ANDROID_MANUFACTURE_SUBSTRING
import com.protego.beaconexchange.helper.Constants.Companion.FOREGROUND_ID
import com.protego.beaconexchange.helper.getForegroundNotification
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit

class BluetoothService : Service() {

    lateinit var scanJob: Disposable
    lateinit var notifySubscribersJob: Disposable
    private val scanner: Scanner by inject()
    private val bluetoothAdvertiser: BluetoothLeAdvertiser by inject()
    private val bluetoothManager: BluetoothManager by inject()

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler {
            // A catch-all error handler
            Timber.e(it, "Error thrown: $it")
        }

        startForeground(FOREGROUND_ID, this.getForegroundNotification(
            "Protego is engaged",
            "You will be warned if you come too close"
        ))

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.extras?.getString(Constants.DEVICE_ID)?.let {
            start(it)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun start(deviceId: String) {
        startAdvertising(deviceId)
        startScanning()
        startServing()
    }

    fun startAdvertising(deviceId: String) {
        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
            .setConnectable(true)
            .setTimeout(0)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
            .build()

        val data = AdvertiseData.Builder()
            .addManufacturerData(
                ANDROID_MANUFACTURE_ID,
                ANDROID_MANUFACTURE_SUBSTRING.toByteArray(StandardCharsets.UTF_8)
            )
            .addServiceUuid(ParcelUuid(UUID.fromString(deviceId)))
            .setIncludeTxPowerLevel(true)
            .build()
        bluetoothAdvertiser.startAdvertising(settings, data, AdCallback())
    }

    fun startScanning() {
        scanJob = scanner.scan()
    }

    fun startServing() {
        val keepaliveCharacteristic = BluetoothGattCharacteristic(
            KEEPALIVE_CHARACTERISTIC_ID,
            BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_NOTIFY or BluetoothGattCharacteristic.PROPERTY_WRITE,
            BluetoothGattCharacteristic.PERMISSION_READ or BluetoothGattCharacteristic.PERMISSION_WRITE
        )
        keepaliveCharacteristic.addDescriptor(
            BluetoothGattDescriptor(
                CLIENT_CONFIG,
                BluetoothGattDescriptor.PERMISSION_READ or BluetoothGattDescriptor.PERMISSION_WRITE
            )
        )
        val service = BluetoothGattService(KEEPALIVE_SERVICE_ID, SERVICE_TYPE_PRIMARY)
        service.addCharacteristic(keepaliveCharacteristic)
        val bluetoothServerCallback = BluetoothServerCallback()
        val server = bluetoothManager.openGattServer(applicationContext, bluetoothServerCallback)
        bluetoothServerCallback.server = server
        server.addService(service)
        var keepAliveCount: Byte = 0x00
        notifySubscribersJob = Observable
            .interval(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .doOnNext {
                if (scanJob.isDisposed) {
                    Timber.i("Scan job stopped ... restarting")
                    startScanning()
                }
                val allDevices = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT)
                val devicesToNotify = allDevices
                    .intersect(bluetoothServerCallback.connectedDevices)
                if (!devicesToNotify.isNullOrEmpty()) {
                    Timber.d("Server sending keepalive to ${devicesToNotify} out of ${allDevices} subscribers")
                    keepaliveCharacteristic.value = byteArrayOf(++keepAliveCount)
                    devicesToNotify.forEach {
                        try {
                            server.notifyCharacteristicChanged(it, keepaliveCharacteristic, false)
                        } catch (ex: Exception) {
                            Timber.e(
                                ex,
                                "Exception when notifying client of keepalive characteristic"
                            )
                        }
                    }
                }
            }.subscribe()
    }

    override fun onDestroy() {
        stop()
        super.onDestroy()
    }

    fun stop() {
        bluetoothAdvertiser.stopAdvertising(AdCallback())
        scanJob.dispose()
        notifySubscribersJob.dispose()
    }

    companion object {
        val SERVICE_ID = UUID.fromString("abf3f75e-b051-4c2e-a669-d715075c73cb")
        val KEEPALIVE_CHARACTERISTIC_ID = UUID.fromString("ffa537a7-9169-486a-b25e-4226e114e085")
        val KEEPALIVE_SERVICE_ID = UUID.fromString("5d52b2b6-c99d-4db9-8fac-a7217d8ed960")
        val CLIENT_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        fun start(context: Context) =
            ContextCompat.startForegroundService(
                context,
                Intent(context, BluetoothService::class.java)
            )
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

private class AdCallback : AdvertiseCallback() {
    override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
        Timber.d("Started advertising service")
    }

    override fun onStartFailure(errorCode: Int) {
        Timber.e("Error attempting to advertise service: ${errorCode}")
    }
}

class BluetoothServerCallback : BluetoothGattServerCallback() {
    var server: BluetoothGattServer? = null
    val connectedDevices = mutableListOf<BluetoothDevice>()
    override fun onCharacteristicReadRequest(
        device: BluetoothDevice,
        requestId: Int,
        offset: Int,
        characteristic: BluetoothGattCharacteristic
    ) {
        Timber.d("Received read request")
        server?.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, byteArrayOf())
        connectedDevices.add(device)
    }

    override fun onConnectionStateChange(
        device: BluetoothDevice?,
        status: Int,
        newState: Int
    ) {
        super.onConnectionStateChange(device, status, newState)
        if (device != null && newState == BluetoothProfile.STATE_DISCONNECTED) {
            Timber.d("device disconnected from server")
            connectedDevices.remove(device)
        } else {
            Timber.d("State changed for device: ${device?.address}, status: ${status}, new state: ${newState}")
        }
    }

    override fun onDescriptorWriteRequest(
        device: BluetoothDevice,
        requestId: Int,
        descriptor: BluetoothGattDescriptor,
        preparedWrite: Boolean,
        responseNeeded: Boolean,
        offset: Int,
        value: ByteArray?
    ) {
        if (BluetoothService.CLIENT_CONFIG == descriptor.uuid) {

            if (Arrays.equals(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE, value)) {
                Timber.d("Added device to connected devices list ${device.address}")

                connectedDevices.add(device)
            } else if (Arrays.equals(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE, value)) {
                Timber.d("Removed device to connected devices list")
                connectedDevices.remove(device)
            } else {
                Timber.e("Called descriptor write but not for any good reason")
            }
            if (responseNeeded)
                server?.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, null)
        } else {
            Timber.i("Received an invalid write request")
            server?.sendResponse(device, requestId, BluetoothGatt.GATT_FAILURE, 0, null)
        }
    }
}
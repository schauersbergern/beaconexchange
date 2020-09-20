package com.protego.beaconexchange.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import com.polidea.rxandroidble2.RxBleClient
import com.protego.beaconexchange.bluetooth.ProximityEventListener
import com.protego.beaconexchange.bluetooth.ScanEventListener
import com.protego.beaconexchange.bluetooth.Scanner

import org.koin.dsl.module


fun bluetoothManager(applicationContext: Context): BluetoothManager =
    getSystemService(applicationContext, BluetoothManager::class.java)!!

fun bluetoothAdvertiser(bluetoothManager: BluetoothManager): BluetoothLeAdvertiser =
    bluetoothManager.adapter.bluetoothLeAdvertiser

fun rxBluetoothClient(applicationContext: Context): RxBleClient =
    RxBleClient.create(applicationContext)

fun bluetoothAdapter(): BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

fun scanEventListener(
    /*repo: ContactRepository,
    proximityNotification: ProximityNotification*/
): ScanEventListener =
    ProximityEventListener(
        /*repo, proximityNotification*/
    )


val bluetoothModule = module {
    single { bluetoothManager(get()) }
    single { bluetoothAdvertiser(get()) }
    single { rxBluetoothClient(get()) }
    single { scanEventListener() }
    single { Scanner(get(), get()) }
}
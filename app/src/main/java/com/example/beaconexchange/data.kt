package com.example.beaconexchange

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BluetoothMessage(
    val blueToothName : String,
    val blueToothAddress : String,
    val distCentimeters: Int,
    val rssi: Int ) : Parcelable
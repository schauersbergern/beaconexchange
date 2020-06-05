package com.protego.beaconexchange.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BluetoothMessage(
    val deviceId: String,
    val blueToothName : String,
    val blueToothAddress : String,
    val distCentimeters: Int,
    val rssi: Int
) : Parcelable
package com.example.localdatasource.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Settings(
    @PrimaryKey
    val id: Int = 0,
    val rssi: Int,
    val ringtone: String,
    val isAlarmActive: Boolean,
    val isVibrationActive: Boolean,
    val loggingEnabled: Boolean
)
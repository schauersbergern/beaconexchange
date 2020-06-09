package com.protego.localdatasource.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppData(
    @PrimaryKey val id: Int,
    val generatedKey: String,
    val deviceName: String
)
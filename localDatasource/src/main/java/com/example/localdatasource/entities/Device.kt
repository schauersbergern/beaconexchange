package com.example.localdatasource.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Device (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val uid : String
)
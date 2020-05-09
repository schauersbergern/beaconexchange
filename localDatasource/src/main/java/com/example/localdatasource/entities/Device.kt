package com.example.localdatasource.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Device (
    @PrimaryKey val id: Int,
    val uid : String
)
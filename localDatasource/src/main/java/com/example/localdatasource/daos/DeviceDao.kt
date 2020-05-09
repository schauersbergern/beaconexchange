package com.example.localdatasource.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.localdatasource.entities.Device

@Dao
interface DeviceDao {
    @Query("SELECT * FROM device")
    fun getAll(): List<Device>


    @Query("SELECT * FROM device where :uid")
    fun findByUid(uid: String): Device

    @Insert
    fun insert(vararg device: Device)

    @Delete
    fun delete(device: Device)
}
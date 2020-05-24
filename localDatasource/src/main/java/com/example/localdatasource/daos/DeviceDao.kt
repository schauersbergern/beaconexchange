package com.example.localdatasource.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.localdatasource.entities.Device

@Dao
interface DeviceDao {
    @Query("SELECT * FROM device")
    fun getAll(): LiveData<List<Device>>

    @Query("SELECT * FROM device where :uid")
    fun findByUid(uid: String): Device

    @Insert
    suspend fun insert(vararg device: Device)

    @Delete
    fun delete(device: Device)
}
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

    @Query("SELECT * FROM device where uid = :uid")
    fun findByUid(uid: String): Device

    @Query("DELETE FROM device where uid = :uid")
    suspend fun deleteByUid(uid: String)

    @Insert
    suspend fun insert(vararg device: Device)

    @Delete
    suspend fun delete(device: Device)
}
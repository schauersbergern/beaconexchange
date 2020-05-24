package com.example.localdatasource

import androidx.lifecycle.LiveData
import com.example.localdatasource.daos.DeviceDao
import com.example.localdatasource.entities.Device

class WhiteListRepository(private val deviceDao: DeviceDao) {
    fun getWhiteList() : LiveData<List<Device>>{
        return deviceDao.getAll()
    }
    suspend fun addToWhiteList(deviceId: String) {
        deviceDao.insert(Device(uid = deviceId))
    }
}
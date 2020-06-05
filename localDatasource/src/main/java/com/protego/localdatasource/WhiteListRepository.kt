package com.protego.localdatasource

import androidx.lifecycle.LiveData
import com.protego.localdatasource.daos.DeviceDao
import com.protego.localdatasource.entities.Device

class WhiteListRepository(private val deviceDao: DeviceDao) {
    fun getWhiteList() : LiveData<List<Device>>{
        return deviceDao.getAll()
    }
    suspend fun addToWhiteList(deviceId: String) {
        deviceDao.insert(Device(uid = deviceId))
    }

    suspend fun deleteFromWhiteList(deviceId: String) {
        deviceDao.deleteByUid(deviceId)
    }
}
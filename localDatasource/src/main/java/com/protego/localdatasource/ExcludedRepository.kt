package com.protego.localdatasource

import androidx.lifecycle.LiveData
import com.protego.localdatasource.daos.DeviceDao
import com.protego.localdatasource.entities.Device

class ExcludedRepository(private val deviceDao: DeviceDao) {
    fun getExcluded() : LiveData<List<Device>>{
        return deviceDao.getAll()
    }
    suspend fun addToExcluded(deviceId: String) {
        deviceDao.insert(Device(uid = deviceId))
    }

    suspend fun deleteFromExcluded(deviceId: String) {
        deviceDao.deleteByUid(deviceId)
    }
}
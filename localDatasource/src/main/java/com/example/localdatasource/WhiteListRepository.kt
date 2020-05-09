package com.example.localdatasource

import com.example.localdatasource.daos.DeviceDao
import com.example.localdatasource.entities.Device

class WhiteListRepository(private val deviceDao: DeviceDao) {
    fun getWhiteList() : List<Device>{
        return deviceDao.getAll()
    }
}
package com.example.localdatasource

import com.example.localdatasource.daos.AppDataDao
import com.example.localdatasource.entities.AppData
import java.util.*

class AppDataRepository(private val dataDao: AppDataDao) {

    fun getDeviceId() : String{
        val data = dataDao.getAppData()

        return if (data != null && data.generatedKey != "") {
            data.generatedKey
        } else {
            generateAndSaveAppID()
        }
    }

    private fun generateAndSaveAppID() : String {
        val uuid = UUID.randomUUID().toString()
        dataDao.insert(AppData(0, uuid))
        return uuid
    }

}
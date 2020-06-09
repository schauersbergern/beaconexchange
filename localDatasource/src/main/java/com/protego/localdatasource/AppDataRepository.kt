package com.protego.localdatasource

import android.content.Context
import com.protego.localdatasource.daos.AppDataDao
import com.protego.localdatasource.entities.AppData
import com.protego.namegenerator.RandomNameGenerator
import java.util.*

class AppDataRepository(private val dataDao: AppDataDao, private val ctx: Context) {

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
        val deviceName = RandomNameGenerator(ctx).next()
        dataDao.insert(AppData(0, uuid, deviceName))
        return uuid
    }

}
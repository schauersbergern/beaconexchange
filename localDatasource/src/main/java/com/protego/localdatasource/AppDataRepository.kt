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
        val id = rand_string()
        val deviceName = RandomNameGenerator(ctx).next()
        dataDao.insert(AppData(0, id, deviceName))
        return id
    }

    private fun rand_string() : String {
        val leftLimit = 33 // letter !
        val rightLimit = 126 // letter ''
        val targetStringLength = 6
        val random = Random()
        val buffer = StringBuilder(targetStringLength)
        for (i in 0 until targetStringLength) {
            val randomLimitedInt =
                leftLimit + (random.nextFloat() * (rightLimit - leftLimit + 1)).toInt()
            buffer.append(randomLimitedInt.toChar())
        }
        return buffer.toString()
    }

}
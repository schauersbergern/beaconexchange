package com.protego.localdatasource

import androidx.lifecycle.LiveData
import com.protego.localdatasource.daos.SettingsDao
import com.protego.localdatasource.entities.Settings

class SettingsRepository(val settingsDao: SettingsDao) {

    fun getLiveSettings() : LiveData<Settings> {
        return settingsDao.getLiveSettings()
    }

    /*
    fun getSettings() : Settings {
        return settingsDao.get()
    }*/

    suspend fun updateSettings(settings: Settings) {
        settingsDao.insert(settings)
    }
}
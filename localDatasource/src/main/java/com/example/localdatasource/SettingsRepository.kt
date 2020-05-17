package com.example.localdatasource

import androidx.lifecycle.LiveData
import com.example.localdatasource.daos.SettingsDao
import com.example.localdatasource.entities.Settings

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
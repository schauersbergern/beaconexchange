package com.protego.beaconexchange.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.protego.localdatasource.LocalDatabase
import com.protego.localdatasource.SettingsRepository
import com.protego.localdatasource.entities.Settings
import kotlinx.coroutines.launch


class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    var settings: LiveData<Settings>

    private var repository: SettingsRepository
    private var app = application

    init {
        val settingsDao = LocalDatabase.getDatabase(app).settingsDao()
        repository = SettingsRepository(settingsDao)
        settings = repository.getLiveSettings()
    }

    fun updateAlarmUri(uri: String) = viewModelScope.launch {
        settings.value?.apply {
            repository.updateSettings(this.copy(ringtone = uri))
        }
    }

    fun toggleAlarm(active: Boolean) = viewModelScope.launch {
        settings.value?.apply {
            repository.updateSettings(this.copy(isAlarmActive = active))
        }
    }

    fun toggleVibration(active: Boolean) = viewModelScope.launch {
        settings.value?.apply {
            repository.updateSettings(this.copy(isVibrationActive = active))
        }
    }

    fun toggleLogging(active: Boolean) = viewModelScope.launch {
        settings.value?.apply {
            repository.updateSettings(this.copy(loggingEnabled = active))
        }
    }

    fun setRssi(rssi: Int) = viewModelScope.launch {
        settings.value?.apply {
            repository.updateSettings(this.copy(rssi = rssi))
        }
    }

    fun saveAll(settings: Settings) = viewModelScope.launch {
        repository.updateSettings(settings)
    }

    fun isLoggingEnabled() : Boolean {
        return settings.value?.loggingEnabled ?: false
    }
}
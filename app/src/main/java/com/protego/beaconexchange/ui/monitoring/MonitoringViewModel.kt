package com.protego.beaconexchange.ui.monitoring

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.protego.localdatasource.AppDataRepository
import com.protego.localdatasource.LocalDatabase
import kotlin.concurrent.thread

data class ServiceState(
    val deviceId: String = "",
    val serviceShouldRun : Boolean = false,
    val alarmState: Boolean = false,
    val locationEnabled: Boolean = false,
    val blueToothEnabled: Boolean = false,
    val backgroundEnabled: Boolean = false
)

class MonitoringViewModel(application: Application) : AndroidViewModel(application) {

    var state : MutableLiveData<ServiceState> = MutableLiveData( ServiceState())
    var app = application

    init {
        thread(start = true) {
            val appDao = LocalDatabase.getDatabase(app).appDataDao()
            val repository = AppDataRepository(appDao, application)
            val deviceId = repository.getDeviceId()

            state.postValue(state.value?.copy(
                deviceId = deviceId,
                serviceShouldRun = false,
                alarmState = false))
        }
    }

    fun setAlarm() {
        state.value = state.value?.copy(alarmState = true)
    }

    fun setSurveilance() {
        state.value = state.value?.copy( alarmState = false)
    }

    fun setOff() {
        state.value = state.value?.copy(serviceShouldRun = false, alarmState = false)
    }

    fun enableLocation() {
        state.value = state.value?.copy(locationEnabled = true)
    }

    fun disableLocation() {
        state.value = state.value?.copy(locationEnabled = false)
    }

    fun enableBlueTooth() {
        state.value = state.value?.copy(blueToothEnabled = true)
    }

    fun disableBlueTooth() {
        state.value = state.value?.copy(blueToothEnabled = false)
    }

    fun enableBackGround() {
        state.value = state.value?.copy(backgroundEnabled = true)
    }

    fun disableBackGround() {
        state.value = state.value?.copy(backgroundEnabled = false)
    }

    fun startService() {
        state.value = state.value?.copy(serviceShouldRun = true, alarmState = false)
    }

    fun stopService() {
        state.value = state.value?.copy(serviceShouldRun = false)
    }

    fun startServiceIfAllEnabledOrStop() {
        state.value?.apply {
            if (locationEnabled && blueToothEnabled && backgroundEnabled) {
                startService()
            } else {
                stopService()
            }
        }
    }

}

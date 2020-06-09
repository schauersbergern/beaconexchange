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

class StartViewModel(application: Application) : AndroidViewModel(application) {

    var state : MutableLiveData<ServiceState> = MutableLiveData( ServiceState())
    var app = application

    init {
        thread(start = true) {
            val appDao = LocalDatabase.getDatabase(app).appDataDao()
            val repository = AppDataRepository(appDao, application)
            val deviceId = repository.getDeviceId()

            state.postValue(state.value?.copy(
                deviceId = deviceId,
                serviceShouldRun = true,
                alarmState = false))
        }
    }

    fun setAlarm() {
        state.postValue( state.value?.copy(alarmState = true) )
    }

    fun setSurveilance() {
        state.postValue( state.value?.copy( alarmState = false) )
    }

    fun setOff() {
        state.postValue( state.value?.copy(serviceShouldRun = false, alarmState = false) )
    }

    fun enableLocation() {
        state.postValue( state.value?.copy(locationEnabled = true) )
        toggleMonitoring()
    }

    fun disableLocation() {
        state.postValue( state.value?.copy(locationEnabled = false) )
        toggleMonitoring()
    }

    fun enableBlueTooth() {
        state.postValue( state.value?.copy(blueToothEnabled = true) )
        toggleMonitoring()
    }

    fun disableBlueTooth() {
        state.postValue( state.value?.copy(blueToothEnabled = false) )
        toggleMonitoring()
    }

    fun enableBackGround() {
        state.postValue( state.value?.copy(backgroundEnabled = true) )
        toggleMonitoring()
    }

    fun disableBackGround() {
        state.postValue( state.value?.copy(backgroundEnabled = false) )
        toggleMonitoring()
    }

    fun startService() {
        state.postValue( state.value?.copy(serviceShouldRun = true, alarmState = false) )
    }

    fun stopService() {
        state.postValue( state.value?.copy(serviceShouldRun = false) )
    }

    private fun toggleMonitoring() {
        state.value?.apply {
            if (locationEnabled && blueToothEnabled && backgroundEnabled) {
                startService()
            } else {
                stopService()
            }
        }
    }

}

package com.example.beaconexchange.ui.start

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.localdatasource.AppDataRepository
import com.example.localdatasource.LocalDatabase
import com.example.localdatasource.WhiteListRepository
import kotlin.concurrent.thread

data class ServiceState(
    val deviceId: String,
    val serviceShouldRun : Boolean,
    val alarmState: Boolean
)

class StartViewModel(application: Application) : AndroidViewModel(application) {

    var state : MutableLiveData<ServiceState> = MutableLiveData(ServiceState("", false, false))
    var app = application

    init {
        thread(start = true) {
            val appDao = LocalDatabase.getDatabase(app).appDataDao()
            val repository = AppDataRepository(appDao)
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
        state.postValue( state.value?.copy(serviceShouldRun = true, alarmState = false) )
    }

    fun setOff() {
        state.postValue( state.value?.copy(serviceShouldRun = false, alarmState = false) )
    }

    fun addToWhitelist(deviceId : String) {
        thread(start = true) {
            val deviceDao = LocalDatabase.getDatabase(app).deviceDao()
            val repository = WhiteListRepository(deviceDao)
            repository.addToWhiteList(deviceId)
        }
    }

}

package com.example.beaconexchange.ui.start

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.localdatasource.AppDataRepository
import com.example.localdatasource.LocalDatabase
import kotlin.concurrent.thread

data class ServiceState(
    val deviceId: String,
    val serviceShouldRun : Boolean,
    val alarmState: Boolean
)

class StartViewModel(application: Application) : AndroidViewModel(application) {

    var state : MutableLiveData<ServiceState> = MutableLiveData(ServiceState("", false, false))

    init {
        thread(start = true) {
            val appDao = LocalDatabase.getDatabase(application).appDataDao()
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

}

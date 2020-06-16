package com.protego.beaconexchange.ui.excluded

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.protego.localdatasource.LocalDatabase
import com.protego.localdatasource.ExcludedRepository
import com.protego.localdatasource.entities.Device
import kotlinx.coroutines.launch

class ExcludedViewModel(application: Application) : AndroidViewModel(application) {

    var excludedLive: LiveData<List<Device>>
    var excluded: List<Device> = listOf()

    private var repository: ExcludedRepository
    private var app = application

    init {
        val deviceDao = LocalDatabase.getDatabase(app).deviceDao()
        repository = ExcludedRepository(deviceDao)
        excludedLive = repository.getExcluded()
    }

    fun addToExcluded(deviceId : String) = viewModelScope.launch {
        if (!isInExcluded(deviceId)) {
            repository.addToExcluded(deviceId)
        }
    }

    fun isInExcluded(deviceId : String) : Boolean {
        //TODO: Optimize, use hashmap
        for (device in excluded) {
            if (device.uid == deviceId) {
                return true
            }
        }
        return  false
    }

    fun addExcluded(list : List<Device>) {
        excluded = list
    }

    fun deleteFromExcluded(uid: String) = viewModelScope.launch {
        repository.deleteFromExcluded(uid)
    }
}
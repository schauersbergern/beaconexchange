package com.example.beaconexchange.ui.whitelist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.localdatasource.LocalDatabase
import com.example.localdatasource.WhiteListRepository
import com.example.localdatasource.entities.Device
import kotlinx.coroutines.launch

class WhiteListViewModel(application: Application) : AndroidViewModel(application) {

    var whiteListLive: LiveData<List<Device>>
    var whiteList: List<Device> = listOf()

    private var repository: WhiteListRepository
    private var app = application

    init {
        val deviceDao = LocalDatabase.getDatabase(app).deviceDao()
        repository = WhiteListRepository(deviceDao)
        whiteListLive = repository.getWhiteList()
    }

    fun addToWhitelist(deviceId : String) = viewModelScope.launch {
        repository.addToWhiteList(deviceId)
    }

    fun isInWhitelist(deviceId : String) : Boolean {
        //TODO: Optimize, use hashmap
        for (device in whiteList) {
            if (device.uid == deviceId) {
                return true
            }
        }
        return  false
    }

    fun addWhiteList(list : List<Device>) {
        whiteList = list
    }

    fun deleteFromWhiteList(uid: String) = viewModelScope.launch {
        repository.deleteFromWhiteList(uid)
    }
}
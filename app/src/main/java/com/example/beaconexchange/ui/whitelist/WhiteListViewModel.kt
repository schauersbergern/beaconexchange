package com.example.beaconexchange.ui.whitelist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.localdatasource.LocalDatabase
import com.example.localdatasource.WhiteListRepository
import com.example.localdatasource.entities.Device
import java.util.*
import kotlin.concurrent.thread

data class WhiteListState(
    val devices: List<Device>
)

class WhiteListViewModel(application: Application) : AndroidViewModel(application) {

    var state : MutableLiveData<WhiteListState> = MutableLiveData(WhiteListState(listOf()))
    private var app = application

    init {

        /*
        thread(start = true) {
            val deviceDao = LocalDatabase.getDatabase(app).deviceDao()
            val repository = WhiteListRepository(deviceDao)
            val devices = repository.getWhiteList()
            state.postValue(WhiteListState(devices))
        }*/
        val list = mutableListOf<Device>()

        for (i in 0..200) {
            val uid = UUID.randomUUID().toString().substring(0, 13)
            list.add(Device(i, "Device $uid"))
        }
        state.postValue(WhiteListState(list))

    }

}
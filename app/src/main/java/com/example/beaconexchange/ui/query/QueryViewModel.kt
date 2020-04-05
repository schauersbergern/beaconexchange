package com.example.beaconexchange.ui.query

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.beaconexchange.BeaconDataRepository
import com.example.beaconexchange.beacon.BeaconContact
import kotlinx.coroutines.launch

class QueryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BeaconDataRepository

    val beacons: LiveData<List<BeaconContact>>

    init {
        repository = BeaconDataRepository(application)
        beacons = repository.allBeacons;
    }

    fun insert(vararg beaconContacts: BeaconContact) {
        viewModelScope.launch { repository.insert(*beaconContacts) }
    }

    fun upsert(vararg beaconContacts: BeaconContact) {
        viewModelScope.launch { repository.upsert(*beaconContacts) }
    }

    fun deleteAll() {
        viewModelScope.launch { repository.deleteAll() }
    }

    fun delete(beaconContact: BeaconContact) {
        viewModelScope.launch { repository.delete(beaconContact) }
    }
}

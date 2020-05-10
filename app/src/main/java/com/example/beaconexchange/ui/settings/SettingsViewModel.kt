package com.example.beaconexchange.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlin.concurrent.thread

data class SettingsState(
    val title: String
)

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    var state : MutableLiveData<SettingsState> = MutableLiveData(SettingsState(""))
    private var app = application

    init {
        state.postValue(SettingsState("title"))
    }

}
package com.protego.beaconexchange.di

import com.protego.beaconexchange.AlarmManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val alarmModule = module {
    single { AlarmManager(androidContext()) }
}
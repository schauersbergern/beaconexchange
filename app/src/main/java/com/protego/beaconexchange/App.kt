package com.protego.beaconexchange

import android.app.Application
import com.protego.beaconexchange.di.alarmModule
import com.protego.beaconexchange.di.bluetoothModule
import com.protego.localdatasource.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    alarmModule,
                    bluetoothModule,
                    databaseModule
                )
            )
        }
    }
}
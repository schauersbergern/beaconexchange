package com.example.beaconexchange

import android.app.*
import android.content.Intent
import android.util.Log
import com.example.beaconexchange.Constants.Companion.REGION_ID
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.Region
import org.altbeacon.beacon.powersave.BackgroundPowerSaver
import org.altbeacon.beacon.service.ArmaRssiFilter
import org.altbeacon.beacon.startup.BootstrapNotifier
import org.altbeacon.beacon.startup.RegionBootstrap

class BeaconExchangeApplication : Application(), BootstrapNotifier {

    private lateinit var regionBootstrap : RegionBootstrap
    private lateinit var backgroundPowerSaver : BackgroundPowerSaver
    private lateinit var beaconManager: BeaconManager

    override fun onCreate() {
        super.onCreate()

        beaconManager = BeaconManager.getInstanceForApplication(this)
        regionBootstrap = getRegionBootstrap()
        backgroundPowerSaver = BackgroundPowerSaver(this)

        BeaconManager.setRssiFilterImplClass(ArmaRssiFilter::class.java)
        // Enable/Disable verbose logging
        BeaconManager.setDebug(false)
    }

    private fun getRegionBootstrap() : RegionBootstrap {
        return RegionBootstrap(this, RegionFactory.getRegion())
    }

    fun stopBeaconForegroundService() {
        regionBootstrap.disable()
        beaconManager.disableForegroundServiceScanning()
    }

    fun startBeaconForegroundService() {
        regionBootstrap.disable()
        beaconManager.enableForegroundServiceScanning(
            getBackgroundNotification(getString(R.string.bg_notification_title)),
            456
        )
        beaconManager.setUpForBackgroundRunning()
        regionBootstrap = getRegionBootstrap()
    }

    override fun didDetermineStateForRegion(p0: Int, p1: Region?) {
        Log.d( name(), "didDetermineStateForRegion" )
    }

    override fun didEnterRegion(p0: Region?) {
        Log.d(name(), "Got a didEnterRegion call")
        regionBootstrap.disable()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        this.startActivity(intent)
    }

    override fun didExitRegion(p0: Region?) {
        Log.d(name(), "didExitRegion")
    }

    private fun name() : String {
        return javaClass.simpleName
    }
}
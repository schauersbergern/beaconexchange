package com.example.beaconexchange

import android.app.Application
import android.util.Log
import com.example.beaconexchange.Constants.Companion.REGION_ID
import com.example.beaconexchange.ui.TimedBeaconSimulator
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.Identifier
import org.altbeacon.beacon.Region
import org.altbeacon.beacon.powersave.BackgroundPowerSaver
import org.altbeacon.beacon.service.ArmaRssiFilter
import org.altbeacon.beacon.startup.BootstrapNotifier
import org.altbeacon.beacon.startup.RegionBootstrap

class BeaconExchangeApplication : Application(), BootstrapNotifier {

    private var regionBootstrap : RegionBootstrap? = null
    private var backgroundPowerSaver : BackgroundPowerSaver? = null


    override fun onCreate() {
        super.onCreate()

        val beaconManager = BeaconManager.getInstanceForApplication(this)
        BeaconManager.setRssiFilterImplClass(ArmaRssiFilter::class.java)
        //BeaconManager.setDebug(true)
        regionBootstrap = RegionBootstrap(this, Region(REGION_ID, null, null, null))
        backgroundPowerSaver = BackgroundPowerSaver(this)

        //org.altbeacon.beacon.logging.LogManager.setVerboseLoggingEnabled(true)

        //BeaconManager.setBeaconSimulator(TimedBeaconSimulator())
        //(BeaconManager.getBeaconSimulator() as TimedBeaconSimulator?)?.createTimedSimulatedBeacons()
    }

    override fun didDetermineStateForRegion(p0: Int, p1: Region?) {
        Log.d("", "")
    }

    override fun didEnterRegion(p0: Region?) {
        Log.d("", "")
    }

    override fun didExitRegion(p0: Region?) {
        Log.d("", "")
    }

}
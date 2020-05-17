package com.example.beaconexchange

import android.media.RingtoneManager
import com.example.localdatasource.entities.Settings
import org.altbeacon.beacon.Region

class RegionFactory {
    companion object {
        fun getRegion() : Region {
            return Region(Constants.REGION_ID, null, null, null)
        }
    }
}


fun getDefaultRingtone() : String {
    return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString()
}

fun getStandardSettings() : Settings {
    return Settings(0, -70, getDefaultRingtone(), false, true)
}
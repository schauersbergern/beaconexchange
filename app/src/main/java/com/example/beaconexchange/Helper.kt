package com.example.beaconexchange

import android.app.AlertDialog
import org.altbeacon.beacon.Region

class RegionFactory {
    companion object {
        fun getRegion() : Region {
            return Region(Constants.REGION_ID, null, null, null)
        }
    }
}
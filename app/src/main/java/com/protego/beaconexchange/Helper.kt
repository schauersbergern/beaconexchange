package com.protego.beaconexchange

import android.content.Context
import android.media.RingtoneManager
import android.util.Log
import com.protego.beaconexchange.Constants.Companion.LOG_DIR
import com.protego.beaconexchange.Constants.Companion.LOG_FILE
import com.protego.localdatasource.entities.Settings
import fr.bipi.tressence.file.FileLoggerTree
import org.altbeacon.beacon.Region
import timber.log.Timber

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

fun getStandardSettings(context: Context) : Settings {
    return Settings(
        0,
        context.resources.getInteger(R.integer.rssi_value_max_range),
        getDefaultRingtone(),
        isAlarmActive = false,
        isVibrationActive = true,
        loggingEnabled = false)
}

fun initLogger(ctx : Context) = FileLoggerTree.Builder()
        .withFileName(LOG_FILE)
        .withDirName(LOG_DIR)
        .withSizeLimit(20000)
        .withFileLimit(20)
        .withMinPriority(Log.DEBUG)
        .appendToFile(true)
        .withContext(ctx)
        .build().also {
            Timber.plant(it)
        }
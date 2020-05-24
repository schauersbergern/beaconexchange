package com.example.beaconexchange

import android.content.Context
import android.media.RingtoneManager
import android.util.Log
import com.example.beaconexchange.Constants.Companion.LOG_DIR
import com.example.beaconexchange.Constants.Companion.LOG_FILE
import com.example.localdatasource.entities.Settings
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

fun getStandardSettings() : Settings {
    return Settings(
        0,
        -70,
        getDefaultRingtone(),
        isAlarmActive = false,
        isVibrationActive = true,
        loggingEnabled = false)
}

fun getTimestamp() = (System.currentTimeMillis() / 1000).toString()

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
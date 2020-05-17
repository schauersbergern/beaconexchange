package com.example.beaconexchange

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.example.localdatasource.entities.Settings

class AlarmManager(private val ctx: Context) {
    var r: Ringtone
    val v = ctx.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    var settings : Settings = getStandardSettings()

    init {
        r = syncRingTone()
    }

    fun syncRingTone() : Ringtone{
        return RingtoneManager.getRingtone(ctx, Uri.parse(settings.ringtone))
    }

    fun changeSettings(settings: Settings) {
        this.settings = settings
        r = syncRingTone()
    }

    fun checkRssiDistance(rssi: Int) {
        when (getRssiSeverity(rssi)) {
            SEVERITY_SEVERE -> {
                startAlarm()
                vibrate()
            }
            else -> stopAlarm()
        }
    }

    private fun vibrate(distance: Int) {
        val amplitude = getAmplitude(distance)
        Log.i(name(),"Amplitude is $amplitude")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(400, amplitude))
        } else {
            v.vibrate(250)
        }
    }

    private fun vibrate() {
        if (settings.isVibrationActive) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(400, 250))
            } else {
                v.vibrate(250)
            }
        }
    }

    private fun stopVibrate() {
        v.cancel()
    }

    private fun getAmplitude(distance: Int) : Int {
        val maxDistance = DISTANCE_MAX
        val remaining = maxDistance - distance

        return if (remaining > 0) {
            val factor = remaining.div(maxDistance.toDouble())
            val maxAmplitude = AMPLITUDE_MAX
            (maxAmplitude * factor).toInt()
        } else {
             0
        }
    }

    private fun startAlarm() {
        if (settings.isAlarmActive) {
            r.play()
        } else {
            r.stop()
        }
    }

    private fun stopAlarm() {
        r.stop()
    }

    fun getRssiSeverity(rssi: Int): Int {
        var severity = SEVERITY_NO
        if (rssi > settings.rssi) {
            severity = SEVERITY_SEVERE
        }
        return severity
    }

    companion object {
        const val SEVERITY_NO = 0
        const val SEVERITY_MEDIUM = 2
        const val SEVERITY_SEVERE = 3

        const val DISTANCE_MAX = 200
        const val DISTANCE_MIN = 50

        //const val RSSI_MAX = -60
        //const val RSSI_MIN = -60

        const val AMPLITUDE_MAX = 255
    }

    private fun name(): String {
        return javaClass.simpleName
    }
}

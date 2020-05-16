package com.example.beaconexchange

import android.content.Context
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log

class AlarmManager(private val ctx: Context) {
    var notification: Uri
    var r: Ringtone
    var mPlayer: MediaPlayer
    val v = ctx.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    /*
    fun checkDistance(distance: Int) {

        when (getSeverity(distance)) {
            SEVERITY_MEDIUM -> {
                //stopAlarm()
                vibrate(distance)
            }
            SEVERITY_SEVERE -> {
                //startAlarm()
                vibrate(distance)
            }
            else -> {
                //stopAlarm()
                stopVibrate()
            }
        }
    }*/

    fun checkRssiDistance(rssi: Int) {
        when (getRssiSeverity(rssi)) {
            SEVERITY_SEVERE -> {
                //startAlarm()
                vibrate()
            }
        }
    }

    fun alarmbutton() {
        if (mPlayer.isPlaying) {
            mPlayer.stop()
        } else {
            mPlayer = MediaPlayer.create(ctx, R.raw.alarm)
            mPlayer.start()
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(400, 250))
        } else {
            v.vibrate(250)
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
        mPlayer = MediaPlayer.create(ctx, R.raw.alarm)
        mPlayer.start()
    }

    private fun stopAlarm() {
        mPlayer.stop()
        v.cancel()
    }

    companion object {
        const val SEVERITY_NO = 0
        const val SEVERITY_MEDIUM = 2
        const val SEVERITY_SEVERE = 3

        const val DISTANCE_MAX = 200
        const val DISTANCE_MIN = 50

        const val RSSI_MAX = -60
        const val RSSI_MIN = -60

        const val AMPLITUDE_MAX = 255

        fun getRssiSeverity(rssi: Int): Int {
            var severity = SEVERITY_NO
            if (rssi > RSSI_MAX) {
                severity = SEVERITY_SEVERE
            }
            return severity
        }
    }

    init {
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        r = RingtoneManager.getRingtone(ctx, notification)
        mPlayer = MediaPlayer.create(ctx, R.raw.alarm)
        val maxVolume = 50.toDouble()
        val currVolume = 10.toDouble()
        val log1 = (Math.log(maxVolume - currVolume) / Math.log(maxVolume)).toFloat()
        mPlayer.setVolume(log1, log1) //set volume takes two paramater
    }

    private fun name(): String {
        return javaClass.simpleName
    }
}

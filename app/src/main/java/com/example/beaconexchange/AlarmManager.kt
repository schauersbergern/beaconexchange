package com.example.beaconexchange

import android.content.Context
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class AlarmManager(private val ctx: Context) {
    var notification: Uri
    var r: Ringtone
    var mPlayer: MediaPlayer
    fun checkDistance(distance: Int) {
        val severity = getSeverity(distance)

        if (severity == SEVERITY_MEDIUM) {
            vibrate()
        } else if (severity == SEVERITY_SEVERE) {
            startAlarm()
            vibrate()
        } else {
            stopAlarm()
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

    private fun getSeverity(distance: Int): Int {
        var severity = SEVERITY_NO
        if (distance < 200 && distance > 150) {
            severity = SEVERITY_MEDIUM
        } else if (distance < 150) {
            severity = SEVERITY_SEVERE
        }
        return severity
    }

    fun vibrate() {
        val v = ctx.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(250, 50))
        } else {
            v.vibrate(250)
        }
    }

    private fun startAlarm() {
        mPlayer = MediaPlayer.create(ctx, R.raw.alarm)
        mPlayer.start()
    }

    private fun stopAlarm() {
        mPlayer.stop()
    }

    companion object {
        const val SEVERITY_NO = 0
        const val SEVERITY_MEDIUM = 2
        const val SEVERITY_SEVERE = 3
    }

    init {
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        r = RingtoneManager.getRingtone(ctx, notification)
        mPlayer = MediaPlayer.create(ctx, R.raw.alarm)
    }
}

package com.example.beaconexchange

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat


@Suppress("DEPRECATION")
fun <T> Context.isServiceRunning(service: Class<T>) = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
    .getRunningServices(Integer.MAX_VALUE)
    .any { it.service.className == service.name }

fun Service.startBeaconForegroundService(intent: Intent, channelId : String, title: String) {
    val input = intent.getStringExtra("inputExtra")
    createNotificationChannel(channelId)
    val notificationIntent = Intent(this, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        this,
        0, notificationIntent, 0
    )
    val notification = NotificationCompat.Builder(this, channelId)
        .setContentTitle(title)
        .setContentText(input)
        .setSmallIcon(R.drawable.shield)
        .setContentIntent(pendingIntent)
        .build()

    startForeground(1, notification)
}

private fun Service.createNotificationChannel(channelId : String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val serviceChannel = NotificationChannel(
            channelId, "Foreground Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(NotificationManager::class.java)
        manager!!.createNotificationChannel(serviceChannel)
    }
}
package com.example.beaconexchange

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.beaconexchange.Constants.Companion.SERVICE_CHANNEL
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser


@Suppress("DEPRECATION")
fun <T> Context.isServiceRunning(service: Class<T>) = (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
    .getRunningServices(Integer.MAX_VALUE)
    .any { it.service.className == service.name }

fun Application.getBackgroundNotification(title: String, body: String? = null) : Notification {
    createNotificationChannel(SERVICE_CHANNEL)
    val notificationIntent = Intent(this, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        this,
        0, notificationIntent, 0
    )
    return  NotificationCompat.Builder(this, SERVICE_CHANNEL)
        .setContentTitle(title)
        .setContentText(body)
        .setSmallIcon(R.drawable.shield)
        .setContentIntent(pendingIntent)
        .build()

}

private fun Application.createNotificationChannel(channelId : String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val serviceChannel = NotificationChannel(
            channelId, "Foreground Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(NotificationManager::class.java)
        manager!!.createNotificationChannel(serviceChannel)
    }
}

fun BeaconManager.setUpForBackgroundRunning() {
    setEnableScheduledScanJobs(false)

    val parser = BeaconParser()
    parser.setBeaconLayout(Constants.ALTBEACON)
    beaconParsers.clear()
    beaconParsers.add(parser)
    backgroundScanPeriod = 1000
    backgroundBetweenScanPeriod = 0
}

fun Context.alertDialog(title: String, message: String) : AlertDialog.Builder {
    return AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, null)
}

fun Beacon.getBluetoothMessage() : BluetoothMessage {
    return BluetoothMessage(
        id2.toString(),
        bluetoothName ?: "No Name",
        bluetoothAddress,
        (distance * 100).toInt(),
        rssi)
}

fun Beacon.isProtego() : Boolean{
    return (id1.toString() ==  Constants.PROTEGO_UUID)
}
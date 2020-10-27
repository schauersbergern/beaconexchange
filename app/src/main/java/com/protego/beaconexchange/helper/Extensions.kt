package com.protego.beaconexchange.helper

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.kevalpatel.ringtonepicker.RingtonePickerDialog
import com.kevalpatel.ringtonepicker.RingtonePickerListener
import com.protego.beaconexchange.MainActivity
import com.protego.beaconexchange.R
import com.protego.beaconexchange.helper.Constants.Companion.SERVICE_CHANNEL
import com.protego.beaconexchange.helper.Constants.Companion.WAKELOCK_TIMEOUT
import com.protego.presentationcore.ProgressDialogFragment
import timber.log.Timber
import kotlin.random.Random


@Suppress("DEPRECATION")
fun <T> Context.isServiceRunning(service: Class<T>) =
    (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == service.name }

fun Context.getForegroundNotification(title: String, body: String? = null): Notification {
    createNotificationChannel(SERVICE_CHANNEL)
    val notificationIntent = Intent(this, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        this,
        0, notificationIntent, 0
    )
    return NotificationCompat.Builder(this, SERVICE_CHANNEL)
        .setContentTitle(title)
        .setContentText(body)
        .setSmallIcon(R.drawable.shield)
        .setContentIntent(pendingIntent)
        .build()

}

private fun Context.createNotificationChannel(channelId: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val serviceChannel = NotificationChannel(
            channelId, "Foreground Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(serviceChannel)
    }
}


fun Context.alertDialog(
    title: String,
    message: String,
    positive: () -> Unit,
    negative: () -> Unit
): AlertDialog.Builder =
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int -> positive() }
        .setNegativeButton(android.R.string.no) { _: DialogInterface, _: Int -> negative() }


fun Context.getRingtoneName(uri: Uri) = RingtoneManager.getRingtone(this, uri).getTitle(this)

fun Context.showNotification(title: String?, message: String?, bigMessage: Boolean = false) {
    val channelId = randomString(5)
    val id = Random.nextInt(1200)

    createNotificationChannel(channelId)
    val builder = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.drawable.shield)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .setStyle(NotificationCompat.BigTextStyle().bigText(message))

    title?.apply { builder.setContentTitle(this) }
    message?.apply { builder.setContentText(this) }

    if (bigMessage && message != null) NotificationCompat.BigTextStyle().bigText(message)

    NotificationManagerCompat.from(this).notify(id, builder.build())
}

fun Activity.getWakeLock(): PowerManager.WakeLock {
    return (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
        newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, name()).apply {
            acquire(WAKELOCK_TIMEOUT)
        }
    }
}

fun Activity.openPowerManager() {
    for (intent in Constants.POWERMANAGER_INTENTS) {
        if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            startActivityForResult(intent, Constants.POWERMANAGER_REQUEST)
        }
    }
}

fun Activity.optimizeBattery() {
    val intent = Intent()
    val pkgName: String = packageName
    val pm = getSystemService(Context.POWER_SERVICE) as PowerManager

    if (pm.isIgnoringBatteryOptimizations(pkgName)) return

    intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
    intent.data = Uri.parse("package:$pkgName")
    startActivity(intent)
}

const val PROGRESS_DIALOG_FRAGMENT_TAG = "PROGRESS_DIALOG_FRAGMENT_TAG"

private val Fragment.existingProgressDialog
    get() = childFragmentManager.findFragmentByTag(PROGRESS_DIALOG_FRAGMENT_TAG) as? ProgressDialogFragment

fun Fragment.syncProgressDialogVisibility(show: Boolean = true) =
    if (show) showProgressDialog() else hideProgressDialog()

private fun Fragment.showNewProgressDialog() {
    ProgressDialogFragment().apply { isCancelable = false }
        .show(childFragmentManager, PROGRESS_DIALOG_FRAGMENT_TAG)
    childFragmentManager.executePendingTransactions()
}

fun Fragment.showProgressDialog() {
    val existing = existingProgressDialog ?: return showNewProgressDialog()
    if (existing.isAdded) return
    // After some testing we assume this does not happen.
    // If it does we are save not showing the blocking dialog in these cases.
    Timber.tag(PROGRESS_DIALOG_FRAGMENT_TAG).w("Not added ProgressDialog is still on backstack.")
}

fun Fragment.hideProgressDialog() = existingProgressDialog?.run { if (isAdded) dismiss() }

fun Fragment.showRingtonePicker(current: Uri, listener: RingtonePickerListener) {
    requireActivity().supportFragmentManager.let {
        RingtonePickerDialog.Builder(
            requireContext(),
            it
        ).setTitle(R.string.set_alarmtone)
            .setCurrentRingtoneUri(current)
            .displayDefaultRingtone(true)
            .setPositiveButtonText(getString(R.string.set_alarmtone))
            .setCancelButtonText(getString(R.string.cancel))
            .setPlaySampleWhileSelection(true)
            .setListener(listener)
    }?.apply {
        addRingtoneType(RingtonePickerDialog.Builder.TYPE_NOTIFICATION)
        addRingtoneType(RingtonePickerDialog.Builder.TYPE_RINGTONE)
        addRingtoneType(RingtonePickerDialog.Builder.TYPE_ALARM)
        show()
    }
}

fun Activity.name() = javaClass.simpleName
fun Fragment.name() = javaClass.simpleName
fun Service.name() = javaClass.simpleName
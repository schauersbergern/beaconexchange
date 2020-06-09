package com.protego.permissions.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.protego.permissions.presentation.PermissionFragment.Companion.BATTERY_OPTIMIZATIONS_REQUEST

fun Fragment.ignoresBatteryOptimizations() : Boolean {
    val pkgName: String = requireActivity().packageName
    val pm = requireActivity().getSystemService(Context.POWER_SERVICE) as PowerManager

    return pm.isIgnoringBatteryOptimizations(pkgName)
}

fun Fragment.activateBatteryOptimizations() {
    val intent = Intent()
    val pkgName: String = requireActivity().packageName

    intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
    intent.data = Uri.parse("package:$pkgName")
    startActivityForResult(intent, BATTERY_OPTIMIZATIONS_REQUEST)
}
package com.protego.permissions.presentation

import android.Manifest
import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

fun hasPermissions(context: Context) : Boolean {
    return if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.Q){
        EasyPermissions.hasPermissions(context, *PermissionFragment.permissionsQ)
    } else{
        EasyPermissions.hasPermissions(context, *PermissionFragment.permissions)
    }
}

fun hasBackgroundLocationPermission(context: Context) : Boolean {
    return EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
}
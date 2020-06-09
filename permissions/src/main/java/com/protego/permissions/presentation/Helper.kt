package com.protego.permissions.presentation

import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

fun hasPermissions(context: Context) : Boolean {
    return EasyPermissions.hasPermissions(context, *PermissionFragment.permissions)
}
package com.shubhans.run.presentation.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat

fun ComponentActivity.shouldShowLocationPermissionRationale(): Boolean {
    return shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
}

fun ComponentActivity.shouldShowNotificationPermissionRationale(): Boolean {
    return Build.VERSION.SDK_INT >= 33 && shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_NOTIFICATION_POLICY)
}

fun Context.hasLocationPermission(): Boolean {
    return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
}

fun Context.hasNotificationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= 33) {
        hasPermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY)
    } else true
}

private fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this, permission
    ) == PackageManager.PERMISSION_GRANTED
}
package com.shubhans.core.presentation.ui

import android.annotation.SuppressLint
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.time.Duration

@SuppressLint("DefaultLocale")
fun Duration.formatted(): String {
    val totalSeconds = inWholeSeconds
    val hours = String.format("%02d", totalSeconds / (60 * 60))
    val minutes = String.format("%02d", (totalSeconds % 3600) / 60)
    val seconds = String.format("%02d", (totalSeconds % 60))
    return "$hours:$minutes:$seconds"
}

fun Double.toFormattedKms(): String {
    return "${this.roundToDecimals(1)} km"
}
fun Double.toFormattedKmh(): String {
    return "${roundToDecimals(1)} km/h"
}
fun Int.toFormattedMeters(): String {
    return "$this m"
}
@SuppressLint("DefaultLocale")
fun Duration.toFormattedPace(distanceKm: Double): String {
    if(this == Duration.ZERO || distanceKm <= 0.0) {
        return "-"
    }
    val secondsPerKm = (this.inWholeSeconds / distanceKm).roundToInt()
    val avgPaceMinutes = secondsPerKm / 60
    val avgPaceSeconds = String.format("%02d", secondsPerKm % 60)
    return "$avgPaceMinutes:$avgPaceSeconds / km"
}

private fun Double.roundToDecimals(decimalCount: Int): Double {
    val facter = 10f.pow(decimalCount)
    return round(this * facter) / facter
}
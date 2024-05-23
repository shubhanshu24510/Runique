package com.shubhans.core.presentation.ui

import android.annotation.SuppressLint
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.time.Duration

@SuppressLint("DefaultLocale")
fun Duration.toFormattedString(): String {
    val totalSeconds = inWholeSeconds
    val hours = totalSeconds / 3600
    val minutes = String.format("%02d", totalSeconds / (60 * 60))
    val remainingSeconds = String.format("%02d", totalSeconds % 60)

    return "$hours:$minutes:$remainingSeconds"
}

fun Double.toFormattedKms(): String {
    return "${this.roundToDecimals(1)} km"
}

@SuppressLint("DefaultLocale")
fun Duration.toFormattedPace(distanceKm: Double): String {
    if (this == Duration.ZERO && distanceKm <= 0) {
        return "_"
    }
    val secondsPerKm = (this.inWholeSeconds / distanceKm).roundToInt()
    val avgPaceMinutes = secondsPerKm / 60
    val avgPaceSeconds = String.format("%02d", secondsPerKm % 60)

    return "$avgPaceMinutes:$avgPaceSeconds /km"
}

private fun Double.roundToDecimals(decimalCount: Int): Double {
    val facter = 10f.pow(decimalCount)
    return round(this * facter) / facter
}
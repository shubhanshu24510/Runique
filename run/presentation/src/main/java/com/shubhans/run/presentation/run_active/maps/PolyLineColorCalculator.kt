package com.shubhans.run.presentation.run_active.maps

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.shubhans.core.domain.location.Location
import com.shubhans.core.domain.location.LocationTimeStamp
import kotlin.math.abs

object PolyLineColorCalculator {
    fun locationToColor(locations1: LocationTimeStamp, locations2: LocationTimeStamp): Color {
        val differenceMeters = locations1.location.location.distanceTo(
            locations2.location.location
        )
        val timeDifference =
            abs((locations2.durationTimeStamp - locations1.durationTimeStamp).inWholeSeconds)
        val speedKmH = (differenceMeters / timeDifference) * 3.6

        return interpolateColor(
            speedKmh = speedKmH,
            minSpeed = 5.0,
            maxSpeed = 20.0,
            colorStart = Color.Green,
            colorMid = Color.Yellow,
            colorEnd = Color.Red
        )
    }

    private fun interpolateColor(
        speedKmh: Double,
        minSpeed: Double,
        maxSpeed: Double,
        colorStart: Color,
        colorMid: Color,
        colorEnd: Color
    ): Color {
        val ratio = ((speedKmh - minSpeed) / (maxSpeed - minSpeed)).coerceIn(0.0..1.0)
        val colorInt = if (ratio <= 0.5) {
            val midRatio = ratio / 0.5
            ColorUtils.blendARGB(colorStart.toArgb(), colorMid.toArgb(), midRatio.toFloat())
        } else {
            val midToEndRatio = (ratio - 0.5) / 0.5
            ColorUtils.blendARGB(colorMid.toArgb(), colorEnd.toArgb(), midToEndRatio.toFloat())
        }
        return Color(colorInt)
    }
}
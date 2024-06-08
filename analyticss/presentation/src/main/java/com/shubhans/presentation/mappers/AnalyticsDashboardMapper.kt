package com.shubhans.presentation.mappers

import com.shubhans.core.presentation.ui.formatted
import com.shubhans.core.presentation.ui.toFormattedKm
import com.shubhans.core.presentation.ui.toFormattedKmh
import com.shubhans.domain.AnalyticsValues
import com.shubhans.presentation.AnalyticsState
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

fun AnalyticsValues.toAnalyticsState(): AnalyticsState {
    return AnalyticsState(
        totalDistanceRun = (totalDistanceRun / 1000.0).toFormattedKm(),
        totalTimeRun = totalTimeRun.toFormattedTotalTime(),
        fastestEverRun = fastestEverRun.toFormattedKmh(),
        avgDistance = (avgDistance / 1000.0).toFormattedKm(),
        avgPace = avgPace.seconds.formatted()
    )
}

fun Duration.toFormattedTotalTime(): String {
    val days = toLong(DurationUnit.DAYS)
    val hours = toLong(DurationUnit.HOURS) % 24
    val minutes = toLong(DurationUnit.MINUTES) % 60

    return "${days}d ${hours}h ${minutes}m"
}
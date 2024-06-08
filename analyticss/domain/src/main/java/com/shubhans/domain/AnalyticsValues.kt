package com.shubhans.domain

import kotlin.time.Duration

data class AnalyticsValues(
    val totalDistanceRun: Int =0,
    val totalTimeRun: Duration = Duration.ZERO,
    val fastestEverRun: Double = 0.0,
    val avgDistance: Double = 0.0,
    val avgPace: Double = 0.0,
)

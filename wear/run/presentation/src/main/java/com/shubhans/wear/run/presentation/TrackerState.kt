package com.shubhans.wear.run.presentation

import kotlin.time.Duration

data class TrackerState(
    val elapsedDuration: Duration = Duration.ZERO,
    val heartRate: Int = 0,
    val distanceMeters: Int = 0,
    val trackable: Boolean = false,
    val isRunningStarted: Boolean = false,
    val hasConnectedPhoneNearby: Boolean = false,
    val isRunActive: Boolean = false,
    val canTrackableHeartRate: Boolean = false,
)

package com.shubhans.core.domain.location

import kotlin.time.Duration

data class LocationTimeStamp(
    val location: LocationWAltitude,
    val durationTimeStamp: Duration
)

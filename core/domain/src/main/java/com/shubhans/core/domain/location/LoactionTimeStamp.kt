package com.shubhans.core.domain.location

import kotlin.time.Duration

data class LoactionTimeStamp(
    val location: LocationWIthAltitute,
    val durationTimeStamp: Duration
)

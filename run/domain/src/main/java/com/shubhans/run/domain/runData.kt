package com.shubhans.run.domain

import com.shubhans.core.domain.location.LoactionTimeStamp
import kotlin.time.Duration

data class RunData(
    val distanceMeters: Int = 0,
    val pace: Duration = Duration.ZERO,
    val locations: List<List<LoactionTimeStamp>>  = emptyList(),
)

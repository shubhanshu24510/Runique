package com.shubhans.run.presentation.run_active

import com.shubhans.core.domain.location.Location
import com.shubhans.run.domain.RunData
import kotlin.time.Duration

data class ActiveRunState(
    val elapsedTime: Duration = Duration.ZERO,
    val runData: RunData = RunData(),
    val hasStartedRunning: Boolean = false,
    val shouldTrack: Boolean = false,
    val currentLocation: Location? = null,
    val isRunFinished: Boolean = false,
    val isSavingRun: Boolean = false,
)

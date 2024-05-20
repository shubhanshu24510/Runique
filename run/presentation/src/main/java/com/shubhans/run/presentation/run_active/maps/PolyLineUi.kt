package com.shubhans.run.presentation.run_active.maps

import androidx.compose.ui.graphics.Color
import com.shubhans.core.domain.location.Location

data class PolyLineUi(
    val location1:Location,
    val location2:Location,
    val color:Color
)

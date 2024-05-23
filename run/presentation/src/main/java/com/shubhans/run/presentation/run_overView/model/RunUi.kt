package com.shubhans.run.presentation.run_overView.model

data class RunUi(
    val id: String,
    val duration: String,
    val dateTime: String,
    val distance: String,
    val pace: String,
    val avgSpeed: String,
    val maxSpeed: String,
    val totelElevation: String,
    val mapPictureUrl: String?,
)

package com.shubhans.run.network.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateRunRequest(
    val id: String,
    val durationMills: Long,
    val distanceMeters: Int,
    val epochMillis: Long,
    val lat: Double,
    val long: Double,
    val avgSpeedKmh: Double,
    val maxSpeedKmh: Double,
    val totalElavationMeters: Int
)

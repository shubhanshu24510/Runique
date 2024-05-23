package com.shubhans.run.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class RunDto(
    val id: String,
    val dateTimeUtc:String,
    val durationMills: Long,
    val distanceMeters: Int,
    val lat: Double,
    val long: Double,
    val avgSpeedKmh: Double,
    val maxSpeedKmh: Double,
    val totalElavationMeters: Int,
    val mapPictureUrl:String?
)
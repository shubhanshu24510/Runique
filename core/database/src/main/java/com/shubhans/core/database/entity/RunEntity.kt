package com.shubhans.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bson.types.ObjectId

@Entity
data class RunEntity(
    val durationMills: Long,
    val dateTimeUtc: String,
    val distanceMeters: Int,
    val longitude: Double,
    val latitude: Double,
    val avgSpeedKmh: Double,
    val maxSpeedKmh: Double,
    val totalElevationMeters: Int,
    val mapPictureUrl: String?,
    @PrimaryKey
    val id: String = ObjectId().toHexString()
)

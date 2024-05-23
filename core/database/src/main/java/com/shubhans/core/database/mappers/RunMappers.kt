package com.shubhans.core.database.mappers

import com.shubhans.core.database.entity.RunEntity
import com.shubhans.core.domain.location.Location
import com.shubhans.core.domain.run.Run
import org.bson.types.ObjectId
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun Run.toRunEntity(): RunEntity {
    return RunEntity(
        id = id ?: ObjectId().toHexString(),
        durationMills = duration.inWholeMilliseconds,
        dateTimeUtc = dateTimeUTC.toInstant().toString(),
        distanceMeters = distanceMeters,
        longitude = location.long,
        latitude = location.lat,
        avgSpeedKmh = avgSpeedKmh,
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl,
    )
}

fun RunEntity.toRun(): Run {
    return Run(
        id = id,
        duration = durationMills.milliseconds,
        dateTimeUTC = Instant.parse(dateTimeUtc)
            .atZone(ZoneId.of("UTC")),
        distanceMeters = distanceMeters,
        location = Location(
            long = longitude,
            lat = latitude
        ),
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl
    )
}
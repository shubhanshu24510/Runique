package com.shubhans.run.network.mappers

import com.shubhans.core.domain.location.Location
import com.shubhans.core.domain.run.Run
import com.shubhans.run.network.dto.RunDto
import com.shubhans.run.network.request.CreateRunRequest
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun RunDto.toRun(): Run {
    return Run(
        id = id,
        duration = durationMills.milliseconds,
        dateTimeUTC = Instant.parse(dateTimeUtc).atZone(ZoneId.of("UTC")),
        distanceMeters = distanceMeters,
        location = Location(lat, long),
        maxSpeedKmh = maxSpeedKmh,
        totalElevationMeters = totalElavationMeters,
        mapPictureUrl = mapPictureUrl
    )
}

fun Run.toCreateRunRequest(): CreateRunRequest {
    return CreateRunRequest(
        id = id!!,
        durationMills = duration.inWholeMicroseconds,
        distanceMeters = distanceMeters,
        epochMillis = dateTimeUTC.toEpochSecond(),
        lat = location.lat,
        long = location.long,
        avgSpeedKmh = avgSpeedKmh,
        maxSpeedKmh = maxSpeedKmh,
        totalElavationMeters = totalElevationMeters
    )
}
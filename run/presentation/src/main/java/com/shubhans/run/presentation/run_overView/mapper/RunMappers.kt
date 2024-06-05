package com.shubhans.run.presentation.run_overView.mapper

import com.shubhans.core.domain.run.Run
import com.shubhans.core.presentation.ui.formatted
import com.shubhans.core.presentation.ui.toFormattedKm
import com.shubhans.core.presentation.ui.toFormattedKmh
import com.shubhans.core.presentation.ui.toFormattedMeters
import com.shubhans.core.presentation.ui.toFormattedPace
import com.shubhans.run.presentation.run_overView.model.RunUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Run.toRunUi(): RunUi {
    val dateTimeInLocalTime = dateTimeUtc
        .withZoneSameInstant(ZoneId.systemDefault())
    val formattedDateTime = DateTimeFormatter
        .ofPattern("MMM dd, yyyy - hh:mma")
        .format(dateTimeInLocalTime)

    val distanceKm = distanceMeters / 1000.0

    return RunUi(
        id = id!!,
        duration = duration.formatted(),
        dateTime = formattedDateTime,
        distance = distanceKm.toFormattedKm(),
        avgSpeed = avgSpeedKmh.toFormattedKmh(),
        maxSpeed = maxSpeedKmh.toFormattedKmh(),
        pace = duration.toFormattedPace(distanceKm),
        totelElevation = totalElevationMeters.toFormattedMeters(),
        mapPictureUrl = mapPictureUrl
    )
}
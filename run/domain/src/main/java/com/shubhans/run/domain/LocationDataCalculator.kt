package com.shubhans.run.domain

import com.shubhans.core.domain.location.LocationTimeStamp
import kotlin.math.roundToInt
import kotlin.time.DurationUnit

object LocationDataCalculator {
    fun getTotalDistanceMeters(locations: List<List<LocationTimeStamp>>): Int {
        return locations
            .sumOf { timestampsPerLine ->
                timestampsPerLine.zipWithNext { location1, location2 ->
                    location1.location.location.distanceTo(location2.location.location)
                }.sum().roundToInt()
            }
    }

    fun getMaxSpeedKmh(locations: List<List<LocationTimeStamp>>): Double {
        return locations.maxOf { locationSet ->
            locationSet.zipWithNext { location1, location2 ->
                val distance =
                    location1.location.location.distanceTo(other = location2.location.location)
                val HoursDiffference = (location2.durationTimeStamp - location1.durationTimeStamp)
                    .toDouble(DurationUnit.HOURS)

                if (HoursDiffference == 0.0) {
                    0.0
                } else {
                    (distance / 1000.0) / HoursDiffference
                }
            }.maxOrNull() ?: 0.0

        }
    }
    fun getTotalElevationMeters(locations: List<List<LocationTimeStamp>>): Int {
        return locations.sumOf { timestampsPerLine ->
            timestampsPerLine.zipWithNext { location1, location2 ->
                val altitute1 = location2.location.altitude
                val altitute2 = location1.location.altitude
                (altitute2 - altitute1).coerceAtLeast(0.0)
            }.sum().roundToInt()
        }
    }
}

package com.shubhans.run.location

import android.location.Location
import com.shubhans.core.domain.location.LocationWAltitude

fun Location.toLocationWithAltitude(): LocationWAltitude {
    return LocationWAltitude(
        location =com.shubhans.core.domain.location.Location(
            lat = latitude,
            long = longitude,
        ),
        altitude = altitude
    )
}
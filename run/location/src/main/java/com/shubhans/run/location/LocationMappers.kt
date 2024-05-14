package com.shubhans.run.location

import android.location.Location
import com.shubhans.core.domain.location.LocationWIthAltitute

fun Location.toLocationWithAltitude(): LocationWIthAltitute {
    return LocationWIthAltitute(
        location =com.shubhans.core.domain.location.Location(
            lat = latitude,
            long = longitude,
        ),
        altitude = altitude
    )
}
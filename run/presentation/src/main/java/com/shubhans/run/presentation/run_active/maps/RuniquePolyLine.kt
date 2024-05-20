package com.shubhans.run.presentation.run_active.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline
import com.shubhans.core.domain.location.LoactionTimeStamp

@Composable
fun RuniquePolyLine(
    locations: List<List<LoactionTimeStamp>>
) {
    val polyline = remember(locations) {
        locations.map {
            it.zipWithNext { timeStamp1, timeStamp2 ->
                PolyLineUi(
                    location1 = timeStamp1.location.location,
                    location2 = timeStamp2.location.location,
                    color = PolyLineColorCalculator.locationToColor(
                        locations1 = timeStamp1, locations2 = timeStamp2
                    )
                )

            }

        }
    }
    polyline.forEach { line ->
        line.forEach { polylineUi ->
            Polyline(
                points = listOf(
                    LatLng(polylineUi.location1.lat, polylineUi.location1.long),
                    LatLng(polylineUi.location2.lat, polylineUi.location2.long)
                ), color = polylineUi.color, jointType = JointType.BEVEL
            )
        }
    }
}
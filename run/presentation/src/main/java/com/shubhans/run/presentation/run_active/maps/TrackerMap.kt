package com.shubhans.run.presentation.run_active.maps

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.shubhans.core.domain.location.LoactionTimeStamp
import com.shubhans.core.domain.location.Location
import com.shubhans.core.presentation.designsystem.RunIcon
import com.shubhans.run.presentation.R
import kotlinx.coroutines.flow.combine

@SuppressLint("RememberReturnType")
@Composable
fun TrackerMap(
    isFinishedRun: Boolean,
    currentLocation: Location?,
    locations: List<List<LoactionTimeStamp>>,
    onSnapShot: (Bitmap) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val mapStyle = remember {
        MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
    }
    val cameraPositionState = rememberCameraPositionState()
    val markerState = rememberMarkerState()

    val markerPositionLat by animateFloatAsState(
        targetValue = currentLocation?.long?.toFloat() ?: 0f,
        animationSpec = tween(durationMillis = 500),
        label = "markerPositionLat"
    )
    val markerPositionLong by animateFloatAsState(
        targetValue = currentLocation?.lat?.toFloat() ?: 0f,
        animationSpec = tween(durationMillis = 500),
        label = "markerPositionLong"
    )
    val markerPosition = remember(markerPositionLat, markerPositionLong) {
        LatLng(markerPositionLat.toDouble(), markerPositionLong.toDouble())
    }

    LaunchedEffect(markerPosition, isFinishedRun) {
        if (!isFinishedRun) {
            markerState.position = markerPosition
        }
    }

    LaunchedEffect(currentLocation, isFinishedRun) {
        if (currentLocation != null && !isFinishedRun) {
            val latLong = LatLng(currentLocation.lat, currentLocation.long)
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(latLong, 17f),
            )
        }
    }

    GoogleMap(
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapStyleOptions = mapStyle,
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
        )
    ){
        if(currentLocation !=null && !isFinishedRun){
            MarkerComposable(
                currentLocation,
                state = markerState
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ){
                    Icon(imageVector = RunIcon,
                        contentDescription = stringResource(id = R.string.run_icon) )
                }
            }
        }

    }
}
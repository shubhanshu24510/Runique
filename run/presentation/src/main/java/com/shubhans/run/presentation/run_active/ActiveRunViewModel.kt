package com.shubhans.run.presentation.run_active

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhans.core.domain.location.Location
import com.shubhans.core.domain.run.Run
import com.shubhans.core.domain.run.RunRepository
import com.shubhans.core.domain.utils.Result
import com.shubhans.core.presentation.ui.asUiText
import com.shubhans.run.domain.LocationDataCalculator
import com.shubhans.run.domain.RunningTracker
import com.shubhans.run.presentation.run_active.services.ActiveRunServices
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime

class ActiveRunViewModel(
    private val runningTracker: RunningTracker,
    private val runRepository: RunRepository
) : ViewModel(

) {
    var state by mutableStateOf(
        ActiveRunState(
            shouldTrack = ActiveRunServices.isServiceActive && runningTracker.isTracking.value,
            hasStartedRunning = ActiveRunServices.isServiceActive
        )
    )
        private set
    private val eventChannel = Channel<ActiveRunEvent>()
    val events = eventChannel.receiveAsFlow()

    private val hasLocationPermission = MutableStateFlow(false)

    private val shouldTrack = snapshotFlow { state.shouldTrack }
        .stateIn(viewModelScope, SharingStarted.Lazily, state.shouldTrack)

    private val isTracking =
        combine(
            shouldTrack,
            hasLocationPermission
        ) { shouldTrack, hasLocationPermission ->
            shouldTrack && hasLocationPermission
        }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    init {
        hasLocationPermission
            .onEach { hasLocationPermission ->
                if (hasLocationPermission) {
                    runningTracker.startObservingLocation()

                } else {
                    runningTracker.stopObservingLocation()
                }
            }.launchIn(viewModelScope)

        isTracking
            .onEach { isTracking ->
                runningTracker.setsTracking(isTracking)
            }.launchIn(viewModelScope)

        runningTracker.currentLocation
            .onEach {
                state = state.copy(
                    currentLocation = it?.location
                )

            }.launchIn(viewModelScope)

        runningTracker.elapsedTime
            .onEach {
                state = state.copy(
                    elapsedTime = it
                )
            }.launchIn(viewModelScope)

        runningTracker.runData
            .onEach {
                state = state.copy(
                    runData = it
                )
            }.launchIn(viewModelScope)

    }

    fun onAction(action: ActiveRunAction) {
        when (action) {
            ActiveRunAction.dismissRationalDialog -> TODO()
            ActiveRunAction.onFinishRunClicked -> {
                state = state.copy(
                    isRunFinished = true,
                    isSavingRun = true
                )
            }

            ActiveRunAction.onBackClicked -> {
                state = state.copy(
                    shouldTrack = false
                )
            }

            ActiveRunAction.onRusumeRunClicked -> {
                state = state.copy(
                    shouldTrack = true
                )
            }

            ActiveRunAction.onToggleRunClicked -> {
                state = state.copy(
                    hasStartedRunning = true,
                    shouldTrack = !state.shouldTrack
                )
            }

            is ActiveRunAction.submitLocationPermissionInfo -> {
                hasLocationPermission.value = action.acceptedLocationPermission
                state = state.copy(
                    showLocationRationale = action.showLocationPermissionRationale
                )
            }

            is ActiveRunAction.submitNotificationPermissionInfo -> {
                state = state.copy(
                    showNotificationRationale = action.showNotificationPermissionRationale
                )
            }

            is ActiveRunAction.dismissRationalDialog -> {
                state = state.copy(
                    showLocationRationale = false,
                    showNotificationRationale = false
                )
            }

            is ActiveRunAction.onRunProcessed -> {
                finishedRun(action.mapPictureBytes)

            }

            else -> Unit
        }
    }

    private fun finishedRun(mapPictureBytes: ByteArray) {
        val locations = state.runData.locations
        if (locations.isEmpty() || locations.size <= 1) {
            state = state.copy(isSavingRun = false)
            return
        }
        viewModelScope.launch {
            val run = Run(
                id = null,
                duration = state.elapsedTime,
                dateTimeUTC = ZonedDateTime.now()
                    .withZoneSameInstant(ZoneId.of("UTC")),
                distanceMeters = state.runData.distanceMeters,
                location = state.currentLocation ?: Location(0.0, 0.0),
                maxSpeedKmh = LocationDataCalculator.getMaxSpeedKmh(locations),
                totalElevationMeters = LocationDataCalculator.getTotalElevationMeters(locations),
                mapPictureUrl = null
            )
            runningTracker.finishedRun()

            when(val result = runRepository.upsertRun(run,mapPictureBytes)){
                is Result.Error -> {
                    eventChannel.send(ActiveRunEvent.Error(result.error.asUiText()))
                }
                is Result.Success -> {
                    eventChannel.send(ActiveRunEvent.SavedRun)
                }
            }
            state = state.copy(
                isSavingRun = false
            )
        }
    }
    override fun onCleared() {
        super.onCleared()
        if (!ActiveRunServices.isServiceActive) {
            runningTracker.stopObservingLocation()
        }
    }
}
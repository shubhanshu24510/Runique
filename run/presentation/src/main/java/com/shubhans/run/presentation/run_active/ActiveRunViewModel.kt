package com.shubhans.run.presentation.run_active

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhans.run.domain.RunningTracker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class ActiveRunViewModel(
    private val runningTracker: RunningTracker
) : ViewModel(

) {
    var state by mutableStateOf(ActiveRunState())
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
                runningTracker.setisTracking(isTracking)
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
            ActiveRunAction.onFinishRunClicked -> TODO()
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
        }
    }
}
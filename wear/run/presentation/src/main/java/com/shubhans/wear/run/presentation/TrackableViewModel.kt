package com.shubhans.wear.run.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhans.wear.run.domain.ExerciseTracker
import com.shubhans.wear.run.domain.PhoneConnector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TrackableViewModel(
    private val exerciseTracker: ExerciseTracker,
    private val phoneConnector: PhoneConnector
) : ViewModel() {
    var state by mutableStateOf(TrackerState())
        private set

    private val hasBodySensorsPermission = MutableStateFlow(false)

    init {
        phoneConnector.connectedNode.filterNotNull().onEach { connectedNode ->
                state = state.copy(
                    hasConnectedPhoneNearby = connectedNode.isNearBy
                )
            }.launchIn(viewModelScope)
    }

    fun onAction(action: TrackerAction) {
        when (action) {
            TrackerAction.onFinished -> Unit
            TrackerAction.onToggledClick -> Unit

            is TrackerAction.OnBodySensorPermissionResult -> {
                hasBodySensorsPermission.value = action.isGranted
                if (action.isGranted) {
                    viewModelScope.launch {
                        val isHeartRateTrackingSupported =
                            exerciseTracker.isHeartRateTrackingSupported()
                        state = state.copy(
                            canTrackableHeartRate = isHeartRateTrackingSupported
                        )
                    }
                }
            }
        }
    }
}
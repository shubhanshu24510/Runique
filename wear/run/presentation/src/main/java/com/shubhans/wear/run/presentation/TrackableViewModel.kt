package com.shubhans.wear.run.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shubhans.connectivity.domain.messageing.MessagingAction
import com.shubhans.core.domain.utils.Result
import com.shubhans.wear.run.domain.ExerciseTracker
import com.shubhans.wear.run.domain.PhoneConnector
import com.shubhans.wear.run.domain.RunningTracker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration

class TrackableViewModel(
    private val exerciseTracker: ExerciseTracker,
    private val phoneConnector: PhoneConnector,
    private val runningTracker: RunningTracker
) : ViewModel() {
    var state by mutableStateOf(TrackerState())
        private set

    private val hasBodySensorsPermission = MutableStateFlow(false)

    private val isTracking = snapshotFlow {
        state.isRunActive && state.trackable && state.hasConnectedPhoneNearby
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val eventChannel = Channel<TrackerEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        phoneConnector.connectedNode.filterNotNull()
            .onEach { connectedNode ->
                state = state.copy(
                    hasConnectedPhoneNearby = connectedNode.isNearBy
                )
            }.combine(isTracking) { _, isTracking ->
                if (!isTracking) {
                    phoneConnector.sendActionToPhone(MessagingAction.connectionRequest)
                }
            }.launchIn(viewModelScope)

        runningTracker.isTrackable.onEach { isTrackable ->
            state = state.copy(
                trackable = isTrackable
            )
        }.launchIn(viewModelScope)

        isTracking.onEach { isTracking ->
            val result = when {
                isTracking && !state.isRunningStarted -> {
                    exerciseTracker.startExercise()
                }

                isTracking && state.isRunningStarted -> {
                    exerciseTracker.resumeExercise()
                }

                !isTracking && state.isRunningStarted -> {
                    exerciseTracker.pauseExercise()
                }

                else -> Result.Success(Unit)
            }
            if (result is Result.Error) {
                result.error.toUiText()?.let {
                    eventChannel.send(TrackerEvent.Error(it))
                }
            }
            if (isTracking) {
                state = state.copy(
                    isRunningStarted = true
                )
                runningTracker.SetisTracking(isTracking)
            }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            val isHeartRateTrackingSupported = exerciseTracker.isHeartRateTrackingSupported()
            state = state.copy(canTrackableHeartRate = isHeartRateTrackingSupported)
        }

        runningTracker.heartRate.onEach { heartRate ->
            state = state.copy(
                heartRate = heartRate
            )
        }.launchIn(viewModelScope)

        runningTracker.distanceMeters.onEach { distance ->
            state = state.copy(
                distanceMeters = distance
            )
        }.launchIn(viewModelScope)

        runningTracker
            .elapsedTime
            .onEach {
                state = state.copy(elapsedDuration = it)
            }.launchIn(viewModelScope)
        listenToPhoneAction()
    }

    fun onAction(action: TrackerAction, triggeredOnPhone: Boolean = false) {
        if(!triggeredOnPhone){
            sendActionToPhone(action)
        }
        when (action) {
            TrackerAction.onFinishedClick ->{
                viewModelScope.launch {
                    exerciseTracker.stopExercise()
                    eventChannel.send(TrackerEvent.RunFinished)

                    state =state.copy(
                        elapsedDuration = Duration.ZERO,
                        distanceMeters = 0,
                        heartRate = 0,
                        isRunActive = false,
                        isRunningStarted = false,
                    )
                }
            }
            TrackerAction.onToggledClick ->{
                if(state.trackable){
                    state = state.copy(isRunActive = !state.isRunActive)
                }
            }
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

    private fun sendActionToPhone(action: TrackerAction) {
        viewModelScope.launch {
            val messagingAction = when (action) {
                is TrackerAction.onFinishedClick -> MessagingAction.Finish
                is TrackerAction.onToggledClick -> {
                    if (state.isRunActive) {
                        MessagingAction.Pause
                    } else {
                        MessagingAction.StartOrResume
                    }
                }

                else -> null
            }
            messagingAction?.let {
                val result = phoneConnector.sendActionToPhone(it)
                if (result is Result.Error) {
                    println("Tracker error: ${result.error}")
                }
            }
        }

    }

    private fun listenToPhoneAction() {
        phoneConnector.messingAction
            .onEach { action ->
                when (action) {
                    MessagingAction.Finish -> {
                        onAction(TrackerAction.onFinishedClick, true)
                    }

                    MessagingAction.StartOrResume -> {
                        if (state.trackable) {
                            state = state.copy(isRunActive = true)
                        }
                    }

                    MessagingAction.Pause -> {
                        if (state.trackable) {
                            state = state.copy(isRunActive = false)
                        }
                    }

                    MessagingAction.Trackable -> {
                        state = state.copy(trackable = true)
                    }

                    MessagingAction.Untraceable -> {
                        state = state.copy(trackable = false)
                    }

                    else -> Unit
                }
            }

    }
}
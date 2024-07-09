@file:OptIn(ExperimentalCoroutinesApi::class)

package com.shubhans.wear.run.domain

import com.shubhans.connectivity.domain.messageing.MessagingAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration

class RunningTracker(
    private val exerciseTracker: ExerciseTracker,
    private val watchToPhoneConnector: PhoneConnector,
    applicationScope: CoroutineScope
) {
    private val _heartRate = MutableStateFlow(0)
    val heartRate = _heartRate.asStateFlow()

    private val _isTracking = MutableStateFlow(false)
    val isTracking = _isTracking.asStateFlow()

    private val _isTrackable = MutableStateFlow(false)
    val isTrackable = _isTrackable.asStateFlow()

    val distanceMeters =
        watchToPhoneConnector.messingAction.filterIsInstance<MessagingAction.HeartRateUpdate>()
            .map { it.heartRate }.stateIn(
                applicationScope, SharingStarted.Lazily, 0
            )
    val elapsedTime = watchToPhoneConnector
        .messingAction
        .filterIsInstance<MessagingAction.ElapsedTimeUpdate>()
        .map { it.elapsedTime }
        .stateIn(
            applicationScope,
            SharingStarted.Lazily,
            Duration.ZERO
        )


    init {
        watchToPhoneConnector.messingAction.onEach { action ->
            when (action) {
                is MessagingAction.Trackable -> {
                    _isTrackable.value = true
                }

                is MessagingAction.Untraceable -> {
                    _isTrackable.value = false
                }

                else -> Unit
            }

        }.launchIn(applicationScope)

        watchToPhoneConnector.connectedNode.filterNotNull().onEach {
            exerciseTracker.prepareExercise()
        }.launchIn(applicationScope)

        isTracking.flatMapLatest { isTracking ->
            if (isTracking) {
                exerciseTracker.heartbeat
            } else flowOf()
        }.onEach { heartRate ->
            watchToPhoneConnector.sendActionToPhone(MessagingAction.HeartRateUpdate(heartRate))
        }.launchIn(applicationScope)
    }

    fun SetisTracking(isTracking: Boolean) {
        _isTracking.value = isTracking
    }
}
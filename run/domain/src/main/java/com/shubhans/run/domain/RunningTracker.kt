@file:OptIn(ExperimentalCoroutinesApi::class)

package com.shubhans.run.domain

import com.shubhans.core.domain.Timer
import com.shubhans.core.domain.location.LoactionTimeStamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class RunningTracker(
    private val locationObserver: LocationObserver, private val applicationScope: CoroutineScope
) {
    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    val elapsedTime = _elapsedTime.asStateFlow()

    private val _runData = MutableStateFlow(RunData())
    val runData = _runData.asStateFlow()

    private val _isTracking = MutableStateFlow(false)
    val isTracking = _isTracking.asStateFlow()

    private val isObservingLocation = MutableStateFlow(false)

    val currentLocation = isObservingLocation.flatMapLatest { isObservingLocation ->
        if (isObservingLocation) {
            locationObserver.observerLocation(1000L)
        } else flowOf()
    }.stateIn(
        scope = applicationScope, started = SharingStarted.Lazily, initialValue = null
    )

    init {
        isTracking.onEach { isTracking ->
            if (!isTracking) {
                val newList = buildList {
                    addAll(runData.value.locations)
                    add(emptyList<LoactionTimeStamp>())
                }.toList()
                _runData.update {
                    it.copy(locations = newList)
                }
            }
        }
        isTracking.flatMapLatest { isTracking ->
            if (isTracking) {
                Timer.TimeandEmits()
            } else flowOf()
        }.onEach {
            _elapsedTime.value += it
        }.launchIn(applicationScope)

        currentLocation.filterNotNull().combineTransform(isTracking) { location, isTracking ->
            if (isTracking) {
                emit(location)
            }
        }.zip(_elapsedTime) { location, elapsedTime ->
            LoactionTimeStamp(
                location = location, durationTimeStamp = elapsedTime
            )
        }.onEach { location ->
            val currentLocation = runData.value.locations
            val lastLocationList = if (currentLocation.isNotEmpty()) {
                currentLocation.last() + location
            } else {
                listOf(location)
            }

            val newLocationList = currentLocation.replaceLast(lastLocationList)
            val durationMeters =
                LocationDataCalculator.getTotalDistanceMeters(locations = newLocationList)
            val durationKm = durationMeters / 1000.0
            val currentDuration = location.durationTimeStamp

            val avgSecondPerKm = if (durationKm == 0.0) {
                0
            } else {
                (currentDuration.inWholeSeconds / durationKm).roundToInt()
            }

            _runData.update {
                RunData(
                    locations = newLocationList,
                    distanceMeters = durationMeters,
                    pace = avgSecondPerKm.seconds
                )
            }
        }.launchIn(applicationScope)
    }

    fun setsTracking(isTracking: Boolean) {
        this._isTracking.value = isTracking
    }

    fun startObservingLocation() {
        isObservingLocation.value = true
    }

    fun stopObservingLocation() {
        isObservingLocation.value = false
    }

    fun finishedRun() {
        stopObservingLocation()
        setsTracking(false)
        _elapsedTime.value = Duration.ZERO
        _runData.value = RunData()
    }

    private fun <T> List<List<T>>.replaceLast(replacement: List<T>): List<List<T>> {
        if (this.isEmpty()) {
            return listOf(replacement)
        }
        return this.dropLast(1) + listOf(replacement)
    }
}



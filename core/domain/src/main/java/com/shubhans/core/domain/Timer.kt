package com.shubhans.core.domain

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.microseconds
object Timer {
    fun TimeandEmits(): Flow<Duration> {
        return flow {
            var lastEmitTime = System.currentTimeMillis()
            while (true) {
                delay(200L)
                val currentTime = System.currentTimeMillis()
                val elatedTime = currentTime - lastEmitTime
                emit(elatedTime.microseconds)
                lastEmitTime = currentTime
            }
        }
    }
}
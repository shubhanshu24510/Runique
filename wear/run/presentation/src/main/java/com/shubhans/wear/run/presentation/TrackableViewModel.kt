package com.shubhans.wear.run.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel

class TrackableViewModel : ViewModel() {
    var state by mutableStateOf(TrackerState())
        private set

    fun onAction(action: TrackerAction) {
    }
}
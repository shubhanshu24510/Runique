package com.shubhans.wear.run.presentation

sealed interface TrackerEvent {
    data object RunFinished : TrackerEvent
}
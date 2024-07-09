package com.shubhans.wear.run.presentation

import com.shubhans.core.presentation.ui.UiText

sealed interface TrackerEvent {
    data object RunFinished : TrackerEvent
    data class Error(val message: UiText) : TrackerEvent
}
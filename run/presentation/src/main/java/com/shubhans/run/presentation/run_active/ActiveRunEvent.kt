package com.shubhans.run.presentation.run_active

import com.shubhans.core.presentation.ui.UiText

sealed interface ActiveRunEvent {
    data class Error(val error: UiText) : ActiveRunEvent
    data object SavedRun : ActiveRunEvent
}
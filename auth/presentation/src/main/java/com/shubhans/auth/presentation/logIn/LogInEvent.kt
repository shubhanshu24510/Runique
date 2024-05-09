package com.shubhans.auth.presentation.logIn

import com.shubhans.core.presentation.ui.UiText

sealed interface LogInEvent {
    data object LogInSuccess : LogInEvent
    data class Error(val error: UiText) : LogInEvent
}
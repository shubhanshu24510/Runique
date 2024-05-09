package com.shubhans.auth.presentation.register

import com.shubhans.core.presentation.ui.UiText

sealed interface RegisterEvent {
    data object RegisterSuccess : RegisterEvent
    data class Error(val error: UiText) : RegisterEvent
}
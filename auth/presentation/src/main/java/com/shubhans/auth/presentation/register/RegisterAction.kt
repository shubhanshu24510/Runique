package com.shubhans.auth.presentation.register

sealed interface RegisterAction {
    data object onLoginClick : RegisterAction
    data object onRegisterClick : RegisterAction
    data object ToggleVisibilityClick : RegisterAction
}
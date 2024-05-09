package com.shubhans.auth.presentation.logIn

sealed interface LogInAction {
    data object onLoginClick : LogInAction
    data object onRegisterClick : LogInAction
    data object TogglePasswordVisibilyClick : LogInAction
}
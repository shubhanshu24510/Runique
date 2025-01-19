package com.shubhans.auth.presentation.logIn

import androidx.compose.foundation.text.input.TextFieldState

data class LogInState(
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val passwordVisible: Boolean = false,
    val isLoggingIn: Boolean = false,
    val canLogin: Boolean = false
)

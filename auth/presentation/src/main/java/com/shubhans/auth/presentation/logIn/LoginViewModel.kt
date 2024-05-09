@file:Suppress("OPT_IN_USAGE_FUTURE_ERROR")

package com.shubhans.auth.presentation.logIn

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.shubhans.auth.domain.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(LogInState())
        private set

    private val eventChannel = Channel<LogInEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: LogInAction) {

    }
}
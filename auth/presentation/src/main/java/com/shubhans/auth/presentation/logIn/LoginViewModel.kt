package com.shubhans.auth.presentation.logIn

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhans.auth.domain.AuthRepository
import com.shubhans.auth.domain.UserDataValidater
import com.shubhans.auth.presentation.R
import com.shubhans.core.domain.utils.DataError
import com.shubhans.core.domain.utils.Result
import com.shubhans.core.presentation.ui.UiText
import com.shubhans.core.presentation.ui.asUiText
import com.shubhans.core.presentation.ui.textAsFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userDataValidator: UserDataValidater
) : ViewModel() {
    var state by mutableStateOf(LogInState())
        private set

    private val eventChannel = Channel<LogInEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        combine(state.email.textAsFlow(), state.password.textAsFlow()) { email, password ->
            state = state.copy(
                canLogin = userDataValidator.isValidEmail(
                    email = email.toString().trim()
                ) && password.isNotEmpty()
            )
        }.launchIn(viewModelScope)
    }

    fun onAction(action: LogInAction) {
        when (action) {
            LogInAction.onLoginClick -> login()
            LogInAction.TogglePasswordVisibilyClick -> {
                state = state.copy(
                    passwordVisible = !state.passwordVisible
                )
            }

            else -> Unit
        }
    }

    private fun login() {
        viewModelScope.launch {
            state = state.copy(isLoggingIn = true)
            val result = authRepository.logIn(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString()
            )
            state = state.copy(isLoggingIn = false)
            when (result) {
                is Result.Success -> {
                    eventChannel.send(LogInEvent.LogInSuccess)
                }

                is Result.Error -> {
                    if (result.error == DataError.NetworkError.UNAUTHORIZED) {
                        eventChannel.send(
                            LogInEvent.Error(
                                UiText.StringResource(R.string.error_email_password_incorrect)
                            )
                        )
                    } else {
                        eventChannel.send(LogInEvent.Error(result.error.asUiText()))
                    }
                }
            }
        }
    }
}





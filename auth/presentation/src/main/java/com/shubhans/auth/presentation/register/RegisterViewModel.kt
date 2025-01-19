package com.shubhans.auth.presentation.register

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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userDataValidator: UserDataValidater,
    private val repository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set
    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        state.email.textAsFlow().onEach { email ->
            val isEmailValid = userDataValidator.isValidEmail(email.toString())
            state = state.copy(
                isEmailValid = isEmailValid,
                canRegister = isEmailValid && state.passwordValidationState.isValidPassword
                        && !state.isRegistering
            )
        }
            .launchIn(viewModelScope)

        state.password.textAsFlow().onEach { password ->
            val passwordValidationState = userDataValidator.validatePassword(password.toString())
            state = state.copy(
                passwordValidationState = passwordValidationState,
                canRegister = state.isEmailValid && passwordValidationState.isValidPassword
                        && !state.isRegistering
            )
        }
            .launchIn(viewModelScope)
    }

    fun OnAction(action: RegisterAction) {
        when (action) {
            RegisterAction.onRegisterClick -> register()
            RegisterAction.ToggleVisibilityClick -> state = state.copy(
                isPasswordVisible = !state.isPasswordVisible
            )

            RegisterAction.onLoginClick -> {}
        }
    }

    private fun register() {
        viewModelScope.launch {
            state = state.copy(isRegistering = true)
            val result = repository.register(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString()
            )
            state = state.copy(isRegistering = false)
            when (result) {
                is Result.Success ->
                    eventChannel.send(RegisterEvent.RegisterSuccess)

                is Result.Error -> {
                    if (result.error == DataError.NetworkError.CONFLICT) {
                        eventChannel.send(RegisterEvent.Error(UiText.StringResource(R.string.email_already_registered)))
                    } else {
                        eventChannel.send(RegisterEvent.Error(result.error.asUiText()))
                    }
                }
            }
        }
    }
}
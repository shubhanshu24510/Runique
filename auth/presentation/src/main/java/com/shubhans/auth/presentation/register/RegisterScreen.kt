@file:OptIn(ExperimentalFoundationApi::class)

package com.shubhans.auth.presentation.register

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shubhans.auth.domain.PasswordValidationState
import com.shubhans.auth.domain.UserDataValidater
import com.shubhans.core.presentation.designsystem.RuniqueTheme
import com.shubhans.auth.presentation.R
import com.shubhans.core.presentation.designsystem.CheckIcon
import com.shubhans.core.presentation.designsystem.CrossIcon
import com.shubhans.core.presentation.designsystem.EmailIcon
import com.shubhans.core.presentation.designsystem.Poppins
import com.shubhans.core.presentation.designsystem.RuniqueDarkRed
import com.shubhans.core.presentation.designsystem.RuniqueGray
import com.shubhans.core.presentation.designsystem.RuniqueGreen
import com.shubhans.core.presentation.designsystem.components.GradientBackground
import com.shubhans.core.presentation.designsystem.components.RuniqueActionButton
import com.shubhans.core.presentation.designsystem.components.RuniquePasswordTextField
import com.shubhans.core.presentation.designsystem.components.RuniqueTextField
import com.shubhans.core.presentation.ui.ObserverEvent
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    onSignInClick: () -> Unit,
    onRegisterSuccessful: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    ObserverEvent(viewModel.events){event ->
        when(event){
            RegisterEvent.RegisterSuccess ->{
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    R.string.registration_sucesfull,
                    Toast.LENGTH_LONG).show()
                onRegisterSuccessful()
            }
            is RegisterEvent.Error -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                  event.error.asString(context),
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    RegisterScreen(
        state = viewModel.state,
        onAction = viewModel::OnAction
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState, onAction: (RegisterAction) -> Unit
) {
    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(id = R.string.create_account),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))

            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = Poppins, color = RuniqueGray
                    )
                ) {
                    append(stringResource(id = R.string.already_have_an_account) + " ")
                    pushStringAnnotation(
                        tag = "login", annotation = stringResource(id = R.string.log_in)
                    )
                    withStyle(
                        style = SpanStyle(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append(stringResource(id = R.string.log_in))

                    }
                }
            }
            ClickableText(text = annotatedString, onClick = { offset ->
                annotatedString.getStringAnnotations(
                    tag = "login", start = offset, end = offset
                ).firstOrNull()?.let {
                    onAction(RegisterAction.onLoginClick)
                }
            })
            Spacer(modifier = Modifier.height(50.dp))
            RuniqueTextField(
                state = state.email,
                startIcon = EmailIcon,
                endIcon = if (state.isEmailValid) CheckIcon else null,
                title = stringResource(id = R.string.ema_il),
                hint = stringResource(id = R.string.email_address),
                addInfo = stringResource(id = R.string.must_be_valid_email),
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(16.dp))
            RuniquePasswordTextField(
                state = state.password,
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisibility = {
                    onAction(RegisterAction.ToggleVisibilityClick)
                },
                title = stringResource(id = R.string.password),
                hint = stringResource(id = R.string.password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            PasswordRequirement(
                text = stringResource(
                    id = R.string.at_least_x_characters, UserDataValidater.PASSWORD_MIN_LENGTH
                ), isValid = state.passwordValidationState.hasMinLength
            )
            Spacer(modifier = Modifier.height(4.dp))
            PasswordRequirement(
                text = stringResource(
                    id = R.string.at_least_one_number,
                ), isValid = state.passwordValidationState.hasNumber
            )
            Spacer(modifier = Modifier.height(4.dp))
            PasswordRequirement(
                text = stringResource(
                    id = R.string.contains_lowercase_char,
                ), isValid = state.passwordValidationState.hasLowerCaseCharacter
            )
            Spacer(modifier = Modifier.height(4.dp))
            PasswordRequirement(
                text = stringResource(
                    id = R.string.contains_uppercase_char,
                ), isValid = state.passwordValidationState.hasUpperCaseCharacter
            )
            Spacer(modifier = Modifier.height(32.dp))
            RuniqueActionButton(text = stringResource(id = R.string.register),
                isLoading = state.isRegistering,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.canRegister,
                onClick = {
                    onAction(RegisterAction.onRegisterClick)
                })
        }
    }
}

@Composable
fun PasswordRequirement(
    text: String, isValid: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Icon(

            imageVector = if (isValid) {
                CheckIcon
            } else CrossIcon, contentDescription = null, tint = if (isValid) {
                RuniqueGreen
            } else RuniqueDarkRed
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp
        )
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    RuniqueTheme {
        RegisterScreen(state = RegisterState(
            passwordValidationState = PasswordValidationState(
                hasNumber = false,
                hasLowerCaseCharacter = true,
                hasUpperCaseCharacter = false,
                hasMinLength = true
            )
        ), onAction = {})
    }
}
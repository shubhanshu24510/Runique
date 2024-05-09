package com.shubhans.auth.domain

import com.sun.jdi.Value

class UserDataValidater(
    private val pattenValidator: PattenValidator
) {
    fun isValidEmail(email: String): Boolean {
        return pattenValidator.matches(email.trim())
    }

    fun validatePassword(password: String): PasswordValidationState {
        val hasMinLength = password.length >= PASSWORD_MIN_LENGTH
        val hasDigit = password.any { it.isDigit() }
        val hasLowerCaseCharacter = password.any { it.isLowerCase() }
        val hasUppercaseCaracter = password.any { it.isUpperCase() }

        return PasswordValidationState(
            hasMinLength = hasMinLength,
            hasNumber = hasDigit,
            hasLowerCaseCharacter = hasLowerCaseCharacter,
            hasUpperCaseCharacter = hasUppercaseCaracter
        )
    }

    companion object {
        const val PASSWORD_MIN_LENGTH = 9
    }
}
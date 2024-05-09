package com.shubhans.auth.data.repository

import kotlinx.serialization.Serializable

@Serializable
data class LogInRequest(
    val email: String,
    val password: String
)

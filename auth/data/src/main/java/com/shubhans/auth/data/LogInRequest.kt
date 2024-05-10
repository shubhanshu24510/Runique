package com.shubhans.auth.data

import kotlinx.serialization.Serializable

@Serializable
data class LogInRequest(
    val email: String,
    val password: String
)

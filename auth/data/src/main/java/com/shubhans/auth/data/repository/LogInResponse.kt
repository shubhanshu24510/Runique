package com.shubhans.auth.data.repository

import kotlinx.serialization.Serializable

@Serializable
data class LogInResponse(
    val accessToken: String,
    val refreshToken: String,
    val expirationTimeStamp: Long,
    val userId: String
)

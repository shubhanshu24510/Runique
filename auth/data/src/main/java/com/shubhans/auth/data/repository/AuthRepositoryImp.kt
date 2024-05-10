package com.shubhans.auth.data.repository

import com.shubhans.auth.data.LogInRequest
import com.shubhans.auth.data.LoginResponse
import com.shubhans.auth.data.RegisterRequest
import com.shubhans.auth.domain.AuthRepository
import com.shubhans.core.data.networking.post
import com.shubhans.core.domain.AuthInfo
import com.shubhans.core.domain.SessionStorage
import com.shubhans.core.domain.utils.DataError
import com.shubhans.core.domain.utils.EmptyResult
import com.shubhans.core.domain.utils.Result
import com.shubhans.core.domain.utils.asEmptyDataResult
import io.ktor.client.HttpClient

class AuthRepositoryImp(
    private val httpClient: HttpClient, private val sessionStorage: SessionStorage
) : AuthRepository {
    override suspend fun logIn(
        email: String, password: String
    ): EmptyResult<DataError.NetworkError> {
        val result = httpClient.post<LogInRequest, LoginResponse>(
            route = "/login", body = LogInRequest(email, password)
        )
        if (result is Result.Success) {
            sessionStorage.set(
                AuthInfo(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                    userId = result.data.userId
                )
            )
        }
        return result.asEmptyDataResult()
    }

    override suspend fun register(
        email: String, password: String
    ): EmptyResult<DataError.NetworkError> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/register", body = RegisterRequest(
                email = email, password = password
            )
        )
    }
}
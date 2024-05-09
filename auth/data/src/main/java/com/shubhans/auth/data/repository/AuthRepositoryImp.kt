package com.shubhans.auth.data.repository

import com.shubhans.auth.data.RegisterRequest
import com.shubhans.auth.domain.AuthRepository
import com.shubhans.core.data.networking.post
import com.shubhans.core.domain.utils.DataError
import com.shubhans.core.domain.utils.EmptyResult
import io.ktor.client.HttpClient
import io.ktor.client.request.post

class AuthRepositoryImp(
    private val httpClient: HttpClient
) : AuthRepository {
    override suspend fun register(email: String, password: String): EmptyResult<DataError.NetworkError> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/register",
            body = RegisterRequest(
                email = email,
                password = password
            )
        )
    }
}
package com.shubhans.auth.domain

import com.shubhans.core.domain.utils.DataError
import com.shubhans.core.domain.utils.EmptyResult

interface AuthRepository {
    suspend fun logIn(email: String, password: String): EmptyResult<DataError.NetworkError>
    suspend fun register(email: String, password: String): EmptyResult<DataError.NetworkError>

}
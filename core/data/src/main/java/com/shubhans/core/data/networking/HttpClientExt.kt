package com.shubhans.core.data.networking

import com.shubhans.core.data.BuildConfig
import com.shubhans.core.domain.utils.DataError
import com.shubhans.core.domain.utils.Result
import io.ktor.client.call.body
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.statement.HttpResponse
import io.ktor.client.request.url
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException

suspend inline fun <reified Response : Any> HttpClient.get(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): Result<Response, DataError.NetworkError> {
    return safeCall {
        get {
            url(constructRoute(route))
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }
}


suspend inline fun <reified Response : Any> HttpClient.post(
    route: String,
    body: String
): Result<Response, DataError.NetworkError> {
    return safeCall {
        post {
            url(constructRoute(route))
            setBody(body)
        }
    }
}

suspend inline fun <reified Response : Any> HttpClient.delete(
    route: String,
    queryParameters: Map<String, Any?> = mapOf()
): Result<Response, DataError.NetworkError> {
    return safeCall {
        delete {
            url(constructRoute(route))
            queryParameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }
}

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, DataError.NetworkError> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        e.printStackTrace()
        return Result.Error(DataError.NetworkError.NO_INTERNET)
    } catch (e: UnresolvedAddressException) {
        e.printStackTrace()
        return Result.Error(DataError.NetworkError.SERIALIZATION)
    } catch (e: Exception) {
        if (e is CancellationException)
            e.printStackTrace()
        return (Result.Error(DataError.NetworkError.UNKNOWN))
    }
    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.NetworkError> {
    return when (response.status.value) {
        in 200..299 -> Result.Success(response.body<T>())
        401 -> Result.Error(DataError.NetworkError.UNAUTHORIZED)
        408 -> Result.Error(DataError.NetworkError.REQUEST_TIMEOUT)
        409 -> Result.Error(DataError.NetworkError.CONFLICT)
        413 -> Result.Error(DataError.NetworkError.PAYLOAD_TOO_LARGE)
        429 -> Result.Error(DataError.NetworkError.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(DataError.NetworkError.SERVER_ERROR)
        else -> Result.Error(DataError.NetworkError.UNKNOWN)
    }
}

fun constructRoute(route: String): String {
    return when {
        route.contains(BuildConfig.BASE_URL) -> route
        route.startsWith("/") -> BuildConfig.BASE_URL + route
        else -> BuildConfig.BASE_URL + "/$route"
    }
}
package com.shubhans.run.network

import com.shubhans.core.data.networking.constructRoute
import com.shubhans.core.data.networking.delete
import com.shubhans.core.data.networking.get
import com.shubhans.core.data.networking.safeCall
import com.shubhans.core.domain.run.RemoteRunDataSource
import com.shubhans.core.domain.run.Run
import com.shubhans.core.domain.utils.DataError
import com.shubhans.core.domain.utils.EmptyResult
import com.shubhans.core.domain.utils.Result
import com.shubhans.core.domain.utils.map
import com.shubhans.run.network.dto.RunDto
import com.shubhans.run.network.mappers.toCreateRunRequest
import com.shubhans.run.network.mappers.toRun
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.headers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class KtorRemoteRunDataSource(
    private val httpClient: HttpClient
) : RemoteRunDataSource {

    override suspend fun getRuns(): Result<List<Run>, DataError.NetworkError> {
        return httpClient.get<List<RunDto>>(
            route = "/runs",
        ).map { runDtos ->
            runDtos.map { it.toRun() }
        }
    }

    override suspend fun postRun(
        run: Run, mapPicture: ByteArray
    ): Result<Run, DataError.NetworkError> {
        val createRunRequestJson =
            kotlinx.serialization.json.Json.encodeToString(run.toCreateRunRequest())
        val result = safeCall<RunDto> {
            httpClient.submitFormWithBinaryData(url = constructRoute("/run"), formData = formData {
                append("MAP_PICTURE", mapPicture, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=mappicture.jpg")
                })
                append("RUN_DATA", createRunRequestJson, Headers.build {
                    append(HttpHeaders.ContentType, "text/plain")
                    append(HttpHeaders.ContentDisposition, "form-data; name=\"RUN_DATA\"")
                })
            }) {
                method = HttpMethod.Post
            }
        }
        return result.map {
            it.toRun()
        }
    }

    override suspend fun deleteRun(id: String): EmptyResult<DataError.NetworkError> {
        return httpClient.delete(
            route = "/run", queryParameters = mapOf(
                "id" to id
            )
        )
    }
}
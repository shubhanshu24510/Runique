package com.shubhans.run.network

import com.shubhans.core.data.networking.delete
import com.shubhans.core.data.networking.get
import com.shubhans.core.data.networking.safeCall
import com.shubhans.core.domain.run.RemoteDataSource
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

class KtorRemoteDataSource(
    private val httpClient: HttpClient
) : RemoteDataSource {
    override suspend fun getRuns(): Result<List<Run>, DataError.NetworkError> {
        return httpClient.get<List<RunDto>>(
            route = "/runs"
        ).map { runDtoList ->
            runDtoList.map { it.toRun() }
        }
    }

    override suspend fun PostRun(
        run: Run, mapPicture: ByteArray
    ): Result<Run, DataError.NetworkError> {
        val createRunRequest =
            kotlinx.serialization.json.Json.encodeToString(run.toCreateRunRequest())
        val result = safeCall<RunDto> {
            httpClient.submitFormWithBinaryData(url = "/run", formData = formData {
                append("MAP_PICTURE", mapPicture, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=mappicture.jpeg")
                })
                append("RUN_DATA", createRunRequest, Headers.build {
                    append(HttpHeaders.ContentType, "text/plain")
                    append(HttpHeaders.ContentDisposition, "form-data; name=RUN_DATA")
                })
            }) {
                method = HttpMethod.Post
            }
        }
        return result.map { it.toRun() }
    }

    override suspend fun deteleRun(id: String): EmptyResult<DataError.NetworkError> {
        return httpClient.delete(
            route = "/run", queryParameters = mapOf("id" to id)
        )
    }
}
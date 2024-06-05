package com.shubhans.core.domain.run

import com.shubhans.core.domain.utils.DataError
import com.shubhans.core.domain.utils.EmptyResult
import com.shubhans.core.domain.utils.Result

interface RemoteRunDataSource {
    suspend fun getRuns(): Result<List<Run>, DataError.NetworkError>
    suspend fun postRun(run: Run, mapPicture: ByteArray): Result<Run, DataError.NetworkError>
    suspend fun deleteRun(id: String): EmptyResult<DataError.NetworkError>
}
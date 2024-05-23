package com.shubhans.core.domain.run

import com.shubhans.core.domain.utils.DataError
import com.shubhans.core.domain.utils.EmptyResult
import com.shubhans.core.domain.utils.Result

interface RemoteDataSource {
    suspend fun getRuns(): Result<List<Run>, DataError.NetworkError>
    suspend fun PostRun(run: Run, mapPicture: ByteArray): Result<Run, DataError.NetworkError>
    suspend fun deteleRun(id: String): EmptyResult<DataError.NetworkError>
}
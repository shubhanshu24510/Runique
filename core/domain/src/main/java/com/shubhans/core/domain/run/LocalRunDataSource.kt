package com.shubhans.core.domain.run

import com.shubhans.core.domain.utils.DataError
import com.shubhans.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

typealias RunId = String

interface LocalRunDataSource {
    suspend fun upsertRun(run: Run): Result<RunId, DataError.LocalError>
    suspend fun upsertRuns(runs: List<Run>): Result<List<RunId>, DataError.LocalError>
    suspend fun getRuns(): Flow<List<Run>>
    suspend fun deleteRun(id: String)
    suspend fun deleteAllRuns()
}
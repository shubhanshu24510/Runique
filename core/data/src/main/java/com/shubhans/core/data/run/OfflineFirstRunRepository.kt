package com.shubhans.core.data.run

import com.shubhans.core.domain.run.LocalRunDataSource
import com.shubhans.core.domain.run.RemoteDataSource
import com.shubhans.core.domain.run.Run
import com.shubhans.core.domain.run.RunId
import com.shubhans.core.domain.run.RunRepository
import com.shubhans.core.domain.utils.DataError
import com.shubhans.core.domain.utils.EmptyResult
import com.shubhans.core.domain.utils.Result
import com.shubhans.core.domain.utils.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class OfflineFirstRunRepository(
    private val localDataSource: LocalRunDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val applicationScope: CoroutineScope
) : RunRepository {
    override fun getRuns(): Flow<List<Run>> {
        return localDataSource.getRuns()
    }

    override suspend fun fetchRuns(): EmptyResult<DataError> {
        return when (val result = remoteDataSource.getRuns()) {
            is Result.Error -> {
                result.asEmptyDataResult()
            }

            is Result.Success -> {
                applicationScope.async {
                    localDataSource.upsertRuns(result.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun upsertRun(run: Run, mapPicture: ByteArray): EmptyResult<DataError> {
        val localResult = localDataSource.upsertRun(run)
        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }
        val runWithId = run.copy(id = localResult.data)
        val remoteResult = remoteDataSource.PostRun(
            runWithId, mapPicture
        )

        return when (remoteResult) {
            is Result.Error -> {
                //Fetch the run from the local data source and update the remote data source
                Result.Success(Unit)
            }

            is Result.Success -> {
                applicationScope.async {
                    localDataSource.upsertRun(remoteResult.data).asEmptyDataResult()
                }.await()
            }
        }
    }
    override suspend fun deleteRun(id: RunId) {
        localDataSource.deleteRun(id)
        val remoteResult = applicationScope.async {
            remoteDataSource.deteleRun(id)
        }.await()
    }
}
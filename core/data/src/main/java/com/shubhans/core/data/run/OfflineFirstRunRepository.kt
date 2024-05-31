package com.shubhans.core.data.run

import com.shubhans.core.database.dto.RunPendingSyncDao
import com.shubhans.core.database.mappers.toRun
import com.shubhans.core.domain.SessionStorage
import com.shubhans.core.domain.run.LocalRunDataSource
import com.shubhans.core.domain.run.RemoteDataSource
import com.shubhans.core.domain.run.Run
import com.shubhans.core.domain.run.RunId
import com.shubhans.core.domain.run.RunRepository
import com.shubhans.core.domain.run.SyncRunScheduler
import com.shubhans.core.domain.utils.DataError
import com.shubhans.core.domain.utils.EmptyResult
import com.shubhans.core.domain.utils.Result
import com.shubhans.core.domain.utils.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfflineFirstRunRepository(
    private val localDataSource: LocalRunDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val applicationScope: CoroutineScope,
    private val runPendingSyncDao: RunPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val syncRunScheduler: SyncRunScheduler
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
            run = runWithId, mapPicture = mapPicture
        )
        return when (remoteResult) {
            is Result.Error -> {
                syncRunScheduler.scheduleSync(
                        SyncRunScheduler.SyncType.CreateRuns(
                            run = runWithId, mapPictureByte = mapPicture
                        )
                    )
                remoteResult.asEmptyDataResult()
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
        val isPendingSync = runPendingSyncDao.getRunPendingSyncsEntity(id) != null
        if (isPendingSync) {
            runPendingSyncDao.deleteRunPendingSyncEntity(id)
            return
        }
        val remoteResult = applicationScope.async {
            remoteDataSource.deteleRun(id)
        }.await()

        if (remoteResult is Result.Error) {
            applicationScope.launch {
                syncRunScheduler.scheduleSync(
                        SyncRunScheduler.SyncType.DeleteSync(
                            runId = id
                        )
                    )
            }.join()
        }
    }

    override suspend fun syncPendingRuns() {
        withContext(Dispatchers.IO) {
            val userId = sessionStorage.get()?.userId ?: return@withContext
            val createdRuns = async {
                runPendingSyncDao.getAllRunPendingSyncsEntities(userId)
            }
            val deleteRuns = async {
                runPendingSyncDao.getAllRunPendingSyncsEntities(userId)
            }

            val createJobs = createdRuns.await().map {
                    launch {
                        val run = it.run.toRun()
                        when (remoteDataSource.PostRun(run, it.mapPictureUrl)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    runPendingSyncDao.deleteRunPendingSyncEntity(it.run.id)
                                }.join()
                            }
                        }
                    }
                }
            val deletedJobs = deleteRuns.await().map {
                    launch {
                        when (remoteDataSource.deteleRun(it.run.id)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    runPendingSyncDao.deleteRunPendingSyncEntity(it.run.id)
                                }.join()
                            }
                        }
                    }
                }
            createJobs.forEach { it.join() }
            deletedJobs.forEach { it.join() }
        }
    }
}

package com.shubhans.core.data.run

import com.shubhans.core.data.networking.get
import com.shubhans.core.database.dto.RunPendingSyncDao
import com.shubhans.core.database.mappers.toRun
import com.shubhans.core.domain.SessionStorage
import com.shubhans.core.domain.run.LocalRunDataSource
import com.shubhans.core.domain.run.RemoteRunDataSource
import com.shubhans.core.domain.run.Run
import com.shubhans.core.domain.run.RunId
import com.shubhans.core.domain.run.RunRepository
import com.shubhans.core.domain.run.SyncRunScheduler
import com.shubhans.core.domain.utils.DataError
import com.shubhans.core.domain.utils.EmptyResult
import com.shubhans.core.domain.utils.Result
import com.shubhans.core.domain.utils.asEmptyDataResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfflineFirstRunRepository(
    private val localRunDataSource: LocalRunDataSource,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val applicationScope: CoroutineScope,
    private val runPendingSyncDao: RunPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val syncRunScheduler: SyncRunScheduler,
    private val client: HttpClient
) : RunRepository {

    override fun getRuns(): Flow<List<Run>> {
        return localRunDataSource.getRuns()
    }

    override suspend fun fetchRuns(): EmptyResult<DataError> {
        return when (val result = remoteRunDataSource.getRuns()) {
            is Result.Error -> result.asEmptyDataResult()
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRuns(result.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun upsertRun(run: Run, mapPicture: ByteArray): EmptyResult<DataError> {
        val localResult = localRunDataSource.upsertRun(run)
        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }

        val runWithId = run.copy(id = localResult.data)
        val remoteResult = remoteRunDataSource.postRun(
            run = runWithId, mapPicture = mapPicture
        )

        return when (remoteResult) {
            is Result.Error -> {
                applicationScope.launch {
                    syncRunScheduler.scheduleSync(
                        syncType = SyncRunScheduler.SyncType.CreateRuns(
                            run = runWithId, mapPictureByte = mapPicture
                        )
                    )
                }.join()
                Result.Success(Unit)
            }

            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRun(remoteResult.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun deleteRun(id: RunId) {
        localRunDataSource.deleteRun(id)

        // Edge case where the run is created in offline-mode,
        // and then deleted in offline-mode as well. In that case,
        // we don't need to sync anything.
        val isPendingSync = runPendingSyncDao.getRunPendingSyncsEntity(id) != null
        if (isPendingSync) {
            runPendingSyncDao.deleteRunPendingSyncEntity(id)
            return
        }

        val remoteResult = applicationScope.async {
            remoteRunDataSource.deleteRun(id)
        }.await()

        if (remoteResult is Result.Error) {
            applicationScope.launch {
                syncRunScheduler.scheduleSync(
                    syncType = SyncRunScheduler.SyncType.DeleteSync(id)
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
            val deletedRuns = async {
                runPendingSyncDao.getAllDeleteRunSyncEntities(userId)
            }

            val createJobs = createdRuns.await().map {
                launch {
                    val run = it.run.toRun()
                    when (remoteRunDataSource.postRun(run, it.mapPictureBytes)) {
                        is Result.Error -> Unit
                        is Result.Success -> {
                            applicationScope.launch {
                                runPendingSyncDao.deleteRunPendingSyncEntity(it.runId)
                            }.join()
                        }
                    }
                }
            }
            val deleteJobs = deletedRuns.await().map {
                launch {
                    when (remoteRunDataSource.deleteRun(it.runId)) {
                        is Result.Error -> Unit
                        is Result.Success -> {
                            applicationScope.launch {
                                runPendingSyncDao.deleteDeleteRunSyncEntity(it.runId)
                            }.join()
                        }
                    }
                }
            }

            createJobs.forEach { it.join() }
            deleteJobs.forEach { it.join() }
        }
    }

    override suspend fun deleteAllRuns() {
        localRunDataSource.deleteAllRuns()
    }

    override suspend fun logout(): EmptyResult<DataError.NetworkError> {
        val result = client.get<Unit>(
            route = "/logout"
        ).asEmptyDataResult()

        client.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>().firstOrNull()
            ?.clearToken()

        return result
    }
}
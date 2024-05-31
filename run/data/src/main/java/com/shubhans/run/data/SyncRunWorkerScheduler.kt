package com.shubhans.run.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.shubhans.core.database.dto.RunPendingSyncDao
import com.shubhans.core.database.entity.DeleteRunSyncEntity
import com.shubhans.core.database.entity.RunPendingSyncEntity
import com.shubhans.core.database.mappers.toRunEntity
import com.shubhans.core.domain.SessionStorage
import com.shubhans.core.domain.run.Run
import com.shubhans.core.domain.run.RunId
import com.shubhans.core.domain.run.SyncRunScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class SyncRunWorkerScheduler(
    private val context: Context,
    private val pendingSyncDao: RunPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope
) : SyncRunScheduler {
    private val workmanager = WorkManager.getInstance(context)
    override suspend fun scheduleSync(syncType: SyncRunScheduler.SyncType) {
        when (syncType) {
            is SyncRunScheduler.SyncType.FetchRuns -> {
                SeduleFetchRuns(syncType.interval)
            }

            is SyncRunScheduler.SyncType.DeleteSync -> {
                SeduledDeleteRun(syncType.runId)
            }

            is SyncRunScheduler.SyncType.CreateRuns -> {
                ScheduleCreateRun(
                    run = syncType.run,
                    mapPictureByte = syncType.mapPictureByte
                )
            }
        }
    }

    private suspend fun ScheduleCreateRun(run: Run, mapPictureByte: ByteArray) {
        val userId = sessionStorage.get()?.userId ?: return
        val pendingSyncEntity = RunPendingSyncEntity(
            run = run.toRunEntity(),
            userId = userId,
            mapPictureUrl = mapPictureByte
        )
        pendingSyncDao.upsertRunPendingSyncEntity(pendingSyncEntity)

        val workRequest =
            OneTimeWorkRequestBuilder<CreateRunWorker>()
                .addTag("create_work")
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                ).setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.EXPONENTIAL,
                    backoffDelay = 2000L,
                    timeUnit = TimeUnit.MILLISECONDS
                ).setInputData(
                    Data.Builder().putString(CreateRunWorker.RUN_ID, pendingSyncEntity.runId)
                        .build()
                ).build()

        applicationScope.launch {
            workmanager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun SeduledDeleteRun(runId: RunId) {
        val userId = sessionStorage.get()?.userId ?: return
        val entity = DeleteRunSyncEntity(
            userId = userId, runId = runId
        )
        pendingSyncDao.upsertDeleteRunSyncEntity(entity)

        val workRequest =
            OneTimeWorkRequestBuilder<DeleteRunWorker>().addTag("delete_work").setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            ).setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            ).setInputData(
                Data.Builder().putString(DeleteRunWorker.RUN_ID, entity.runId).build()
            ).build()

        applicationScope.launch {
            workmanager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun SeduleFetchRuns(interval: Duration) {
        val isSyncScheduled = withContext(Dispatchers.IO) {
            workmanager.getWorkInfosByTag("sync_work").get().isNotEmpty()
        }
        if (isSyncScheduled) return

        val workRequest = PeriodicWorkRequestBuilder<FetchDataWorker>(
            repeatInterval = interval.toJavaDuration(),
        ).setConstraints(
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.EXPONENTIAL,
            backoffDelay = 2000L,
            timeUnit = TimeUnit.MILLISECONDS
        ).setInitialDelay(
            duration = 30, timeUnit = TimeUnit.MINUTES
        ).addTag("sync_work").build()
        workmanager.enqueue(workRequest).await()
    }

    override suspend fun cancelAllScheduledSync() {
        WorkManager.getInstance(context).cancelAllWork().await()
    }

}
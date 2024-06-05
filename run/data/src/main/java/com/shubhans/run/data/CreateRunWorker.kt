package com.shubhans.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shubhans.core.database.dto.RunPendingSyncDao
import com.shubhans.core.database.mappers.toRun
import com.shubhans.core.domain.run.RemoteRunDataSource

class CreateRunWorker(
    val context: Context,
    private val params: WorkerParameters,
    private val remoteDataSource: RemoteRunDataSource,
    private val pendingSyncDao: RunPendingSyncDao
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }
        val pendingRunId = inputData.getString(RUN_ID) ?: return Result.failure()
        val pendingRunEntity =
            pendingSyncDao.getRunPendingSyncsEntity(pendingRunId) ?: return Result.failure()

        val run = pendingRunEntity.run.toRun()
        return when (val result = remoteDataSource.postRun(run, pendingRunEntity.mapPictureBytes)) {
            is com.shubhans.core.domain.utils.Result.Error -> {
                result.error.toWorkerResult()
            }

            is com.shubhans.core.domain.utils.Result.Success -> {
                pendingSyncDao.deleteRunPendingSyncEntity(pendingRunId)
                Result.success()
            }
        }
    }

    companion object {
        const val RUN_ID = "RunId"
    }
}
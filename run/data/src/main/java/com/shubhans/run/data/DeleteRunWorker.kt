package com.shubhans.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shubhans.core.database.dto.RunPendingSyncDao
import com.shubhans.core.domain.run.RemoteRunDataSource

class DeleteRunWorker(
    val context: Context,
    private val params: WorkerParameters,
    private val remoteDataSource: RemoteRunDataSource,
    private val pendingSyncDao: RunPendingSyncDao
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }
        val runId = inputData.getString(RUN_ID) ?: return Result.failure()
        return when (val result = remoteDataSource.deleteRun(runId)) {
            is com.shubhans.core.domain.utils.Result.Error -> {
                result.error.toWorkerResult()
            }

            is com.shubhans.core.domain.utils.Result.Success -> {
                pendingSyncDao.deleteDeletedRunSyncEntity(runId)
                Result.success()
            }
        }
    }

    companion object {
        const val RUN_ID = "RunId"
    }
}
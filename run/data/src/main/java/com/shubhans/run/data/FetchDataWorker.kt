package com.shubhans.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shubhans.core.domain.run.RunRepository
class FetchDataWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: RunRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }
        return when (val result = repository.fetchRuns()) {
            is com.shubhans.core.domain.utils.Result.Error -> {
                result.error.toWorkerResult()
            }

            is com.shubhans.core.domain.utils.Result.Success -> {
                Result.success()
            }
        }
    }
}
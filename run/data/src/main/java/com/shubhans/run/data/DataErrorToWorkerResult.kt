package com.shubhans.run.data

import androidx.work.ListenableWorker
import com.shubhans.core.domain.utils.DataError

fun DataError.toWorkerResult(): ListenableWorker.Result {
    return when (this) {
        DataError.LocalError.DISK_FULL -> ListenableWorker.Result.failure()
        DataError.NetworkError.REQUEST_TIMEOUT -> ListenableWorker.Result.retry()
        DataError.NetworkError.UNAUTHORIZED -> ListenableWorker.Result.retry()
        DataError.NetworkError.CONFLICT -> ListenableWorker.Result.retry()
        DataError.NetworkError.TOO_MANY_REQUESTS -> ListenableWorker.Result.retry()
        DataError.NetworkError.NO_INTERNET -> ListenableWorker.Result.retry()
        DataError.NetworkError.PAYLOAD_TOO_LARGE -> ListenableWorker.Result.failure()
        DataError.NetworkError.SERVER_ERROR -> ListenableWorker.Result.retry()
        DataError.NetworkError.SERIALIZATION -> ListenableWorker.Result.failure()
        DataError.NetworkError.UNKNOWN -> ListenableWorker.Result.failure()
    }
}
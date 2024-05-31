package com.shubhans.core.domain.run

import kotlin.time.Duration

interface SyncRunScheduler {
    suspend fun scheduleSync(syncType: SyncType)
    suspend fun cancelAllScheduledSync()
    sealed interface SyncType {
        data class FetchRuns(val interval: Duration) : SyncType
        data class DeleteSync(val runId: RunId) : SyncType
        class CreateRuns(val run: Run, val mapPictureByte: ByteArray) : SyncType
    }
}
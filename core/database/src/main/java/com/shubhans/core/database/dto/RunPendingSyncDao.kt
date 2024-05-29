package com.shubhans.core.database.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.shubhans.core.database.entity.DeleteRunSyncEntity
import com.shubhans.core.database.entity.RunPendingSyncEntity

@Dao
interface RunPendingSyncDao {
    //created SyncDao to handle the sync operations
    @Query("SELECT * FROM runpendingsyncentity WHERE userId = :userId")
    suspend fun getAllRunPendingSyncsEntities(userId: String): List<RunPendingSyncEntity>

    @Query("SELECT * FROM runpendingsyncentity WHERE runId = :runId")
    suspend fun getRunPendingSyncsEntity(runId: String): RunPendingSyncEntity?

    @Upsert
    suspend fun upsertRunPendingSyncEntity(entity: RunPendingSyncEntity)

    @Query("DELETE  FROM runpendingsyncentity WHERE runId = :runId")
    suspend fun deleteRunPendingSyncEntity(runId: String)

    //deleted syncopal to handle the sync operations

    @Query("SELECT * FROM runpendingsyncentity WHERE userId = :userId")
    suspend fun getAllDeleteRunSyncEntities(userId: String): List<DeleteRunSyncEntity>

    @Upsert
    suspend fun upsertDeleteRunSyncEntity(entity: DeleteRunSyncEntity)

    @Query("DELETE  FROM runpendingsyncentity WHERE runId = :runId")
    suspend fun deleteDeleteRunSyncEntity(runId: String)
}


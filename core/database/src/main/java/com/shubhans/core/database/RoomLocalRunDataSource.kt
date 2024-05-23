package com.shubhans.core.database

import com.shubhans.core.database.dao.RunDao
import com.shubhans.core.database.mappers.toRun
import com.shubhans.core.database.mappers.toRunEntity
import com.shubhans.core.domain.run.LocalRunDataSource
import com.shubhans.core.domain.run.Run
import com.shubhans.core.domain.run.RunId
import com.shubhans.core.domain.utils.DataError
import com.shubhans.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalRunDataSource(
    private val runDao: RunDao
) : LocalRunDataSource {
    override suspend fun upsertRun(run: Run): Result<RunId, DataError.LocalError> {
        return try {
            val entity = run.toRunEntity()
            runDao.upsetRun(entity)
            Result.Success(entity.id)
        } catch (e: Exception) {
            Result.Error(DataError.LocalError.DISK_FULL)
        }
    }

    override suspend fun upsertRuns(runs: List<Run>): Result<List<RunId>, DataError.LocalError> {
        return try {
            val entities = runs.map { it.toRunEntity() }
            runDao.upsetRuns(entities)
            Result.Success(entities.map { it.id })
        } catch (e: Exception) {
            Result.Error(DataError.LocalError.DISK_FULL)
        }
    }

    override suspend fun getRuns(): Flow<List<Run>> {
        return runDao.getRuns()
            .map { entities ->
                entities.map { it.toRun() }
            }
    }

    override suspend fun deleteRun(id: String) {
        runDao.deleteRun(id)
    }

    override suspend fun deleteAllRuns() {
        runDao.deleteAllRuns()
    }
}
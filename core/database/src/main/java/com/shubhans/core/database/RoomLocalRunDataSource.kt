package com.shubhans.core.database

import android.database.sqlite.SQLiteFullException
import com.shubhans.core.database.dto.RunDao
import com.shubhans.core.database.mappers.toRun
import com.shubhans.core.database.mappers.toRunEntity
import com.shubhans.core.domain.run.LocalRunDataSource
import com.shubhans.core.domain.run.Run
import com.shubhans.core.domain.run.RunId
import com.shubhans.core.domain.utils.DataError
import com.shubhans.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.sql.SQLDataException

class RoomLocalRunDataSource(
    private val runDao: RunDao
) : LocalRunDataSource {
    override suspend fun upsertRun(run: Run): Result<RunId, DataError.LocalError> {
        return try {
            val entity = run.toRunEntity()
            runDao.upsertRun(entity)
            Result.Success(entity.id)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.LocalError.DISK_FULL)
        }
    }

    override suspend fun upsertRuns(runs: List<Run>): Result<List<RunId>, DataError.LocalError> {
        return try {
            val entities = runs.map { it.toRunEntity() }
            runDao.upsertRuns(entities)
            Result.Success(entities.map { it.id })
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.LocalError.DISK_FULL)
        }
    }

    override suspend fun getRuns(): Flow<List<Run>> {
        return runDao.getRuns().map { runs ->
                runs.map { it.toRun() }
            }
    }

    override suspend fun deleteRun(id: String) {
        runDao.deleteRun(id = id)
    }

    override suspend fun deleteAllRuns() {
        deleteAllRuns()
    }
}
package com.shubhans.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shubhans.core.database.dto.RunDao
import com.shubhans.core.database.dto.RunPendingSyncDao
import com.shubhans.core.database.entity.DeleteRunSyncEntity
import com.shubhans.core.database.entity.RunEntity
import com.shubhans.core.database.entity.RunPendingSyncEntity

@Database(
    entities = [RunEntity::class,
        RunPendingSyncEntity::class,
        DeleteRunSyncEntity::class],
    version = 1
)
abstract class RunDatabase : RoomDatabase() {
    abstract fun runDao(): RunDao
    abstract fun runPendingSyncDao(): RunPendingSyncDao
}
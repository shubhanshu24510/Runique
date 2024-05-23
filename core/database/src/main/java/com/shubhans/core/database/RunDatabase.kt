package com.shubhans.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shubhans.core.database.dao.RunDao
import com.shubhans.core.database.entity.RunEntity

@Database(
    entities = [RunEntity::class],
    version = 1
)
abstract class RunDatabase:RoomDatabase() {
    abstract fun runDao(): RunDao
}
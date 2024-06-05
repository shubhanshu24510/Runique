package com.shubhans.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DeleteRunSyncEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    val runId: String
)

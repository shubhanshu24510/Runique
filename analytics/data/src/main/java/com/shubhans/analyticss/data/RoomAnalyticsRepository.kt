package com.shubhans.analyticss.data

import com.shubhans.core.database.dto.AnalyticsDao
import com.shubhans.domain.AnalyticsRepository
import com.shubhans.domain.AnalyticsValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.microseconds

class RoomAnalyticsRepository(
    private val analyticsDao: AnalyticsDao
) : AnalyticsRepository {
    override suspend fun getAnalyticsValues(): AnalyticsValues {
        return withContext(Dispatchers.IO) {
            val totalDistanceRun = async { analyticsDao.getTotalDistance() }
            val totalTimeMills = async { analyticsDao.getTotalTimeRun() }
            val maxRunSpeed = async { analyticsDao.getMaxRunSpeed() }
            val avgDistancePerRun = async { analyticsDao.getAvgDistancePerRun() }
            val avgPacePerRun = async { analyticsDao.getAvgPacePerRun() }

            AnalyticsValues(
                totalDistanceRun = totalDistanceRun.await(),
                totalTimeRun = totalTimeMills.await().microseconds,
                fastestEverRun = maxRunSpeed.await(),
                avgDistance = avgDistancePerRun.await(),
                avgPace = avgPacePerRun.await()
            )
        }
    }
}
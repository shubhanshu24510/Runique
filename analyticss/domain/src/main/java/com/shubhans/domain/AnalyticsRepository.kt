package com.shubhans.domain

interface AnalyticsRepository {
    suspend fun getAnalyticsValues(): AnalyticsValues
}
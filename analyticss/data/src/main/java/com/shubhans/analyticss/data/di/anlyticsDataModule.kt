package com.shubhans.analyticss.data.di

import com.shubhans.analyticss.data.RoomAnalyticsRepository
import com.shubhans.core.database.RunDatabase
import com.shubhans.core.database.dto.AnalyticsDao
import com.shubhans.domain.AnalyticsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsModule = module {
    singleOf(::RoomAnalyticsRepository).bind<AnalyticsRepository>()

    single {
        get<RunDatabase>().analyticsDao
    }
}
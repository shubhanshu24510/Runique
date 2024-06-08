package com.shubhans.presentation.di

import com.shubhans.presentation.AnalyticsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val analyticsPresentationModule = module {
    singleOf(::AnalyticsViewModel)
}
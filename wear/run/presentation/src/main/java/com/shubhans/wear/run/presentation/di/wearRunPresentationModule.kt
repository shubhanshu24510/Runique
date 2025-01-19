package com.shubhans.wear.run.presentation.di

import com.shubhans.wear.run.presentation.TrackableViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val wearRunPresentationModule = module {
    viewModelOf(::TrackableViewModel)
}

package com.shubhans.run.presentation.di

import com.shubhans.run.domain.RunningTracker
import com.shubhans.run.presentation.run_active.ActiveRunViewModel
import com.shubhans.run.presentation.run_overView.RunOverViewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val runPresentationModule = module {
    singleOf(::RunningTracker)

    viewModelOf(::RunOverViewViewModel)
    viewModelOf(::ActiveRunViewModel)
}
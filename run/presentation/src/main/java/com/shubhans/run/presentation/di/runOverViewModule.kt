package com.shubhans.run.presentation.di

import com.shubhans.run.presentation.run_active.ActiveRunViewModel
import com.shubhans.run.presentation.run_overView.RunOverViewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val runOverViewmodule = module {
    viewModelOf(::RunOverViewViewModel)
    viewModelOf(::ActiveRunViewModel)
}
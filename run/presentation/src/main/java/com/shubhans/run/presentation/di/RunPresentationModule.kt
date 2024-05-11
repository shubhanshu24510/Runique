package com.shubhans.run.presentation.di

import com.shubhans.run.presentation.RunOverViewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val runPresentationModule = module {
    viewModelOf(::RunOverViewViewModel)
}
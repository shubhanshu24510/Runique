package com.shubhans.auth.presentation

import com.shubhans.auth.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val AuthViewModelModule = module {
    viewModelOf(::RegisterViewModel)
}
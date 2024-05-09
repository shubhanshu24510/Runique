package com.shubhans.auth.presentation.di

import com.shubhans.auth.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authViewModuleModule = module {
    viewModelOf(::RegisterViewModel)
}
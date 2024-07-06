package com.shubhans.wear.app.presentation.di

import com.shubhans.wear.app.presentation.RuniqueApp
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single {
        (androidApplication() as RuniqueApp).applicationScope
    }
}
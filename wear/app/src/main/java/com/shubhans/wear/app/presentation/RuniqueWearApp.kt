package com.shubhans.wear.app.presentation

import android.app.Application
import com.shubhans.wear.run.data.di.wearRunDataModule
import com.shubhans.wear.run.presentation.di.wearRunPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RuniqueWearApp :Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@RuniqueWearApp)
            modules(
                wearRunPresentationModule,
                wearRunDataModule
            )
        }
    }
}
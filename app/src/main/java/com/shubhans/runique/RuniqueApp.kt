package com.shubhans.runique

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat
import com.shubhans.auth.data.di.authDataModule
import com.shubhans.auth.presentation.di.authViewModuleModule
import com.shubhans.connectivity.domain.di.coreConnectivityDataModule
import com.shubhans.core.data.di.coreDataModule
import com.shubhans.core.database.di.databaseModule
import com.shubhans.run.data.di.runDataModule
import com.shubhans.run.location.di.locationModule
import com.shubhans.run.network.di.networkKoinModule
import com.shubhans.run.presentation.di.runPresentationModule
import com.shubhans.runique.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import timber.log.Timber

class RuniqueApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            androidLogger()
            androidContext(this@RuniqueApp)
            workManagerFactory()
            modules(
                authDataModule,
                authViewModuleModule,
                appModule,
                coreDataModule,
                runPresentationModule,
                locationModule,
                databaseModule,
                networkKoinModule,
                runDataModule,
                coreConnectivityDataModule
            )
        }
    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}

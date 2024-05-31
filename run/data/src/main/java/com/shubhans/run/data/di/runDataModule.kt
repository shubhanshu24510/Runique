package com.shubhans.run.data.di

import com.shubhans.core.domain.run.SyncRunScheduler
import com.shubhans.run.data.CreateRunWorker
import com.shubhans.run.data.DeleteRunWorker
import com.shubhans.run.data.FetchDataWorker
import com.shubhans.run.data.SyncRunWorkerScheduler
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchDataWorker)
    workerOf(::DeleteRunWorker)
    singleOf(::SyncRunWorkerScheduler).bind<SyncRunScheduler>()
}
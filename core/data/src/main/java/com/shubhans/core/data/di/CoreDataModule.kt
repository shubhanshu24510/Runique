package com.shubhans.core.data.di

import com.shubhans.core.data.auth.EncryptedSessionStorage
import com.shubhans.core.data.networking.HttpClientFactory
import com.shubhans.core.data.run.OfflineFirstRunRepository
import com.shubhans.core.domain.SessionStorage
import com.shubhans.core.domain.run.RunRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single { HttpClientFactory(get()).build() }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
    singleOf(::OfflineFirstRunRepository).bind<RunRepository>()
}
package com.shubhans.run.network.di

import com.shubhans.core.domain.run.RemoteRunDataSource
import com.shubhans.run.network.KtorRemoteRunDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkKoinModule = module {
    singleOf(::KtorRemoteRunDataSource).bind<RemoteRunDataSource>()
}
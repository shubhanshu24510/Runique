package com.shubhans.run.network.di

import com.shubhans.core.domain.run.RemoteDataSource
import com.shubhans.run.network.KtorRemoteDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkKoinModule = module {
    singleOf(::KtorRemoteDataSource).bind<RemoteDataSource>()
}
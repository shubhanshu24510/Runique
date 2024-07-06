package com.shubhans.connectivity.data.di

import com.shubhans.connectivity.data.NodeDiscovery
import com.shubhans.connectivity.data.WearNodeDiscovery
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreConnectivityDataModule = module {
    singleOf(::WearNodeDiscovery).bind<NodeDiscovery>()
}
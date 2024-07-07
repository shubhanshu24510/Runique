package com.shubhans.connectivity.domain.di

import com.shubhans.connectivity.domain.NodeDiscovery
import com.shubhans.connectivity.domain.WearNodeDiscovery
import com.shubhans.connectivity.domain.messageing.MessageClient
import com.shubhans.connectivity.domain.messaging.WearMessageClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreConnectivityDataModule = module {
    singleOf(::WearNodeDiscovery).bind<NodeDiscovery>()
    singleOf(::WearMessageClient).bind<MessageClient>()
}
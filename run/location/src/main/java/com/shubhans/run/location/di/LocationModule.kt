package com.shubhans.run.location.di

import com.shubhans.run.domain.LocationObserver
import com.shubhans.run.location.AndroidLocationObserver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val locationModule = module {
    singleOf(::AndroidLocationObserver).bind<LocationObserver>()

}
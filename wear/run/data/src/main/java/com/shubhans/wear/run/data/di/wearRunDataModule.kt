package com.shubhans.wear.run.data.di

import androidx.health.services.client.HealthServicesClient
import com.shubhans.wear.run.data.HealthServicesExerciseTracker
import com.shubhans.wear.run.domain.ExerciseTracker
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val wearRunDataModule = module {
    singleOf(::HealthServicesExerciseTracker).bind<ExerciseTracker>()

}
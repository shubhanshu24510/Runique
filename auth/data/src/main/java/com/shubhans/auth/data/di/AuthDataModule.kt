package com.shubhans.auth.data.di

import com.shubhans.auth.data.repository.AuthRepositoryImp
import com.shubhans.auth.domain.AuthRepository
import com.shubhans.auth.domain.PattenValidator
import com.shubhans.auth.domain.UserDataValidater
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<PattenValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidater)
    singleOf(::AuthRepositoryImp).bind<AuthRepository>()
}
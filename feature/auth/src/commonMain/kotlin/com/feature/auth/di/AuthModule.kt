package com.feature.auth.di

import com.feature.auth.contract.AuthSession
import com.feature.auth.repository.AuthRepository
import com.feature.auth.session.AuthSessionImpl
import org.koin.dsl.module

val authModule = module {
    single<AuthSession> { AuthSessionImpl(get()) }
    single { AuthRepository(otpService = get(), session = get(), userProfileService = get()) }
}

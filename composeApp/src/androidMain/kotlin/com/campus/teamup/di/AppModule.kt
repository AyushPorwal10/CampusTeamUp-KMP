package com.campus.teamup.di

import com.campus.teamup.auth.AuthViewModel
import com.campus.teamup.service.FirestoreUserProfileService
import com.feature.auth.contract.UserProfileService
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UserProfileService> { FirestoreUserProfileService() }
    viewModel { AuthViewModel(get()) }
}

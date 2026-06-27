package com.campus.teamup.di

import com.campus.teamup.FirebaseDashboardConfigRepository
import com.campus.teamup.auth.AuthViewModel
import com.campus.teamup.dashboard.DashboardViewModel
import com.campus.teamup.data.DashboardConfigRepository
import com.campus.teamup.domain.GetDashboardLayoutUseCase
import com.campus.teamup.service.FirestoreUserProfileService
import com.feature.auth.contract.UserProfileService
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UserProfileService> { FirestoreUserProfileService() }
    single<DashboardConfigRepository> { FirebaseDashboardConfigRepository(Firebase.firestore) }
    single { GetDashboardLayoutUseCase(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
}

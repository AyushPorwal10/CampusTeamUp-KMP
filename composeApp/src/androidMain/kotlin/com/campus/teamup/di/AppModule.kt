package com.campus.teamup.di

import com.campus.teamup.auth.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { AuthViewModel(get()) }
}

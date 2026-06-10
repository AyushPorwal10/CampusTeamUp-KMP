package com.feature.auth.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.feature.auth.contract.OtpService
import com.feature.auth.service.FirebaseOtpService
import com.feature.auth.util.ActivityProvider
import okio.Path.Companion.toPath
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidAuthModule = module {
    single { ActivityProvider() }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                androidContext().filesDir.resolve("auth_session.preferences_pb").absolutePath.toPath()
            }
        )
    }

    single<OtpService> {
        FirebaseOtpService(activityProvider = get<ActivityProvider>()::get)
    }
}

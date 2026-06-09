package com.campus.teamup

import android.app.Application
import com.campus.teamup.di.appModule
import com.feature.auth.di.androidAuthModule
import com.feature.auth.di.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CampusApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CampusApp)
            modules(androidAuthModule, authModule, appModule)
        }
    }
}

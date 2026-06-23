package com.syncra.pos

import android.app.Application
import com.syncra.pos.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PosApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@PosApplication)
            modules(appModule)
        }
    }
}

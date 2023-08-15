package com.zzb.whichhospital

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Thread.UncaughtExceptionHandler{ _, throwable ->
            throwable.printStackTrace()
        }
    }
}
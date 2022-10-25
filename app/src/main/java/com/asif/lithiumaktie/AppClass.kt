package com.asif.lithiumaktie

import android.app.Application
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatDelegate
import com.onesignal.OneSignal

@Keep
class AppClass : Application() {

    private val ONESIGNAL_APP_ID = "443f00fb-0c9b-4c67-8a07-2c6ff2bad11d"


    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

    }
}
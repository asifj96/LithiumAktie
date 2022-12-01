package com.asif.lithiumaktie.utility

import android.util.Log
import com.asif.lithiumaktie.BuildConfig

object Logger {

    fun info(tag: String, text: String?) {
        if (BuildConfig.DEBUG) Log.i(tag, text ?: "")
    }

    fun debug(tag: String, text: String?) {
        if (BuildConfig.DEBUG) Log.d(tag, text ?: "")
    }

    fun warning(tag: String, text: String?) {
        if (BuildConfig.DEBUG) Log.w(tag, text ?: "")
    }

}

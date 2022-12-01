package com.asif.lithiumaktie.utility

import android.content.Context
import android.content.SharedPreferences
import java.util.*

object PreferenceData {

    private const val SHARED_PREFERENCES_NAME = "PreferenceData.Lithium.Aktie"
    private const val LANGUAGE_CODE = "LanguageCode"

    private fun Context.getSharedPreferences(): SharedPreferences {
        return getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun Context.setLanguageCode(languageCode: String) {
        getSharedPreferences().edit().putString(LANGUAGE_CODE, languageCode)
            .apply()
    }

    fun Context.getLanguageCode(): String {
        val systemDefault = Locale.getDefault().language
        Logger.debug("systemDefault", systemDefault)
        return getSharedPreferences().getString(LANGUAGE_CODE, systemDefault)!!
    }
}
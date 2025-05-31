package com.nihatmahammadli.abbmobile.presentation.dashboard.ui

import android.content.Context
import android.content.res.Configuration
import java.util.Locale
import androidx.core.content.edit

object LocaleHelper {
    fun setAppLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getString("app_lang", "en") ?: "en"
    }

    fun saveLanguage(context: Context, lang: String) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString("app_lang", lang).apply()
    }

}


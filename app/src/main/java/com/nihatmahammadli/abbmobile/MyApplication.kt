package com.nihatmahammadli.abbmobile

import android.app.Application
import android.content.Context
import com.nihatmahammadli.abbmobile.presentation.dashboard.ui.LocaleHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        val lang = LocaleHelper.getSavedLanguage(base!!)
        val context = LocaleHelper.setAppLocale(base, lang)
        super.attachBaseContext(context)
    }
}
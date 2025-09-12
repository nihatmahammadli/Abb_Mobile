package com.nihatmahammadli.abbmobile.presentation.components.dummyData

import android.content.Context
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.presentation.model.SettingsItem

object SettingsData {

    fun getList(context: Context) = listOf(
        SettingsItem(
            android.R.drawable.ic_menu_compass,
            context.getString(R.string.carbon_footprint),
            actionText = context.getString(R.string.carbon_footprint_action)
        ),
        SettingsItem(
            android.R.drawable.ic_menu_day,
            context.getString(R.string.interface_mode),
            context.getString(R.string.interface_mode_value)
        ),
        SettingsItem(
            android.R.drawable.ic_menu_manage,
            context.getString(R.string.application_language),
            context.getString(R.string.application_language_value)
        ),
        SettingsItem(
            android.R.drawable.ic_menu_my_calendar,
            context.getString(R.string.notification_settings),
            context.getString(R.string.notification_settings_value)
        ),
        SettingsItem(
            android.R.drawable.ic_menu_gallery,
            context.getString(R.string.application_icon)
        ),
        SettingsItem(
            android.R.drawable.ic_menu_camera,
            context.getString(R.string.stickers)
        ),
        SettingsItem(
            android.R.drawable.star_big_on,
            context.getString(R.string.rate_us)
        )
    )
}

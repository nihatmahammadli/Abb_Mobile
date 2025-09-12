package com.nihatmahammadli.abbmobile.presentation.model

data class SettingsItem(
    val icon: Int,
    val title: String,
    val subtitle: String? = null,
    val actionText: String? = null
)
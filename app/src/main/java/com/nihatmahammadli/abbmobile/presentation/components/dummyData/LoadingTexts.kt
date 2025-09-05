package com.nihatmahammadli.abbmobile.presentation.components.dummyData

import android.content.Context
import com.nihatmahammadli.abbmobile.R

object LoadingTexts {

    fun getTexts(context: Context): List<String> {
        return listOf(
            context.getString(R.string.loading_card_order),
            context.getString(R.string.loading_checking),
            context.getString(R.string.loading_preparing)
        )
    }
}
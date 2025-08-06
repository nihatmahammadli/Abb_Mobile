package com.nihatmahammadli.abbmobile.presentation.components.providers

import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.presentation.model.BaseCardData

object CardProvider {
    fun getCards(): MutableList<BaseCardData> {
        return mutableListOf(
            BaseCardData.DefaultCard(
                title = "Add new card",
                description = "Add other bank's card \nor order a new one",
                backgroundResId = R.drawable.card_background,
                buttonText = "Apply now",
                showVisa = true
            ),
            BaseCardData.DefaultCard(
                title = "Loan applications",
                description = "Sifariş etdiyin kart yoldadır",
                backgroundResId = R.drawable.card_background_2,
                buttonText = "Apply now",
                showVisa = false
            ),
            BaseCardData.DefaultCard(
                title = "Add value your money",
                description = "Kart bonus balansını yoxla",
                backgroundResId = R.drawable.card_background_3,
                buttonText = "Apply now",
                showVisa = false
            )
        )
    }
}

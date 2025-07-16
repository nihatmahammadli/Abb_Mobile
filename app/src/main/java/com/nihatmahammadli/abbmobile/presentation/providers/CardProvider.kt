package com.nihatmahammadli.abbmobile.presentation.providers

import android.util.Log
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
                title = "Kart izləmə",
                description = "Sifariş etdiyin kart yoldadır",
                backgroundResId = R.drawable.card_background_2,
                buttonText = "İzləməyə bax",
                showVisa = false
            ),
            BaseCardData.DefaultCard(
                title = "Bonuslar",
                description = "Kart bonus balansını yoxla",
                backgroundResId = R.drawable.card_background_3,
                buttonText = "Bonuslara keç",
                showVisa = false
            )
            // İstəsən buraya CustomCard da əlavə et, misal:
            /*
            BaseCardData.CustomCard(
                title = "Mastercard",
                balance = "1000.00 ₼",
                cardCodeEnding = "•••• 1234",
                expiryDate = "12/30",
                backgroundResId = R.drawable.card_background,
                cardLogoResId = R.drawable.visa_card,
                showVisa = true,
                onTopUpClick = { Log.i("CardAction", "Top-up clicked") },
                onPayClick = { Log.i("CardAction", "Pay clicked") },
                onTransferClick = { Log.i("CardAction", "Transfer clicked") }
            )
            */
        )
    }

    fun handleCardClick(position: Int){
        when(position){
            1 -> {
                Log.i("CardClick", "Yeni kart sifarişi")
            }
            2 -> {
                Log.i("CardClick", "Kart izləmə səhifəsi")
            }
            3 -> {
                Log.i("CardClick", "Bonuslar bölməsi")
            }
        }
    }
}

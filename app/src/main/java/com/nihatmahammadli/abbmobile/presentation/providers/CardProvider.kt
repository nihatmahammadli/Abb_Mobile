package com.nihatmahammadli.abbmobile.presentation.providers

import android.util.Log
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.presentation.dashboard.home.model_home.CardData

object CardProvider {
    fun getCards(): List<CardData> {
        return listOf(
            CardData(
                title = "Add new card",
                description = "Add other bank\'s card \nor order a new one",
                backgroundResId = R.drawable.card_background,
                buttonText = "Apply now",
                showVisa = true
            ),CardData(
                title = "Kart izləmə",
                description = "Sifariş etdiyin kart yoldadır",
                backgroundResId = R.drawable.card_background_2,
                buttonText = "İzləməyə bax",
                showVisa = false
            ),
            CardData(
                title = "Bonuslar",
                description = "Kart bonus balansını yoxla",
                backgroundResId = R.drawable.card_background_3,
                buttonText = "Bonuslara keç",
                showVisa = false
            )
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
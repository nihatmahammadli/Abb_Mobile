package com.nihatmahammadli.abbmobile.presentation.providers

import android.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.presentation.dashboard.home.model_home.CardData
import retrofit2.http.POST
import kotlin.math.log

object CardProvider {

    fun getCards(): List<CardData> {
        return listOf(
            CardData(
                title = "Yeni kart sifarişi",
                description = "ABB Visa kartını sifariş et",
                backgroundResId = R.drawable.card_background,
                buttonText = "Sifariş et",
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
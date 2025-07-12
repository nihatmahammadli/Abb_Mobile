package com.nihatmahammadli.abbmobile.data.remote

import com.nihatmahammadli.abbmobile.data.model.CardInfo
import retrofit2.http.GET

interface CardApi {
    @GET("cards")
    suspend fun getCards(): List<CardInfo>

}
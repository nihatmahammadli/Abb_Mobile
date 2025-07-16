package com.nihatmahammadli.abbmobile.domain.repository

import com.nihatmahammadli.abbmobile.data.model.CardInfo

interface CardRepository {
    suspend fun getCards(): List<CardInfo>

    suspend fun getRemoteCards(): List<CardInfo>

}

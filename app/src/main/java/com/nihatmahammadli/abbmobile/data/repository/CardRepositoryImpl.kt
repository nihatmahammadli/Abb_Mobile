package com.nihatmahammadli.abbmobile.data.repository

import com.nihatmahammadli.abbmobile.data.model.CardInfo
import com.nihatmahammadli.abbmobile.data.remote.CardApi
import com.nihatmahammadli.abbmobile.domain.repository.CardRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepositoryImpl @Inject constructor(
    private val cardApi: CardApi
) : CardRepository {
    override suspend fun getCards(): List<CardInfo> {
        return cardApi.getCards()
    }

    override suspend fun getRemoteCards(): List<CardInfo> {
        return cardApi.getCards()
    }
}

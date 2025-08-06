package com.nihatmahammadli.abbmobile.presentation.components.mapper

import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.data.model.CardInfo
import com.nihatmahammadli.abbmobile.domain.model.UiCard

object CardMapper {

    fun mapToUiCard(cardInfo: CardInfo): UiCard {
        return UiCard(
            cardNumber = cardInfo.cardNumber,
            expiryDate = cardInfo.expiryDate,
            cvv = cardInfo.cvv
        )
    }
}

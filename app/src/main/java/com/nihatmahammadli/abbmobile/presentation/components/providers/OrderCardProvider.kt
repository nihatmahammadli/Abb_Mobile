package com.nihatmahammadli.abbmobile.presentation.components.providers

import com.nihatmahammadli.abbmobile.presentation.model.AllCardInfo

object OrderCardProvider {
    fun getCards(): List<AllCardInfo> {
        return listOf(
            AllCardInfo(
                title = "Tam DigiCard",
                price = "FREE",
                priceText = "Price",
                transfer = "FREE",
                transferText = "Cash withawal",
                withDrawal = "7%",
                withDrawalText = "Income on card balance",
                tariff = "FREE",
                tariffText = "Transfer",
                currency = "5 Y.",
                currencyText = "Term",
                welcomeMiles = "X 1.3",
                welcomeMilesText = "Fayda Max",
                cardImage = "https://abb-bank.az/storage/uploads/files/1735458510_tam-digi.png"
            ),
            AllCardInfo(
                title = "Tam Kart",
                price = "3 AZN",
                priceText = "Price",
                transfer = "FREE",
                transferText = "Transfer",
                withDrawal = "FREE",
                withDrawalText = "Cash withdawal",
                tariff = "3%",
                tariffText = "Income on card balance",
                currency = "7 Y.",
                currencyText = "Term",
                welcomeMiles = "X 1.3",
                welcomeMilesText = "Fayda Max",
                cardImage = "https://abb-bank.az/storage/uploads/files/1735459566_tamkart.png"
            ),
            AllCardInfo(
                title = "Tam Kart Premium",
                price = "100 AZN",
                priceText = "Price",
                transfer = "FREE",
                transferText = "Transfer",
                withDrawal = "FREE",
                withDrawalText = "Cash withdawal",
                tariff = "7%",
                tariffText = "Income on card balance",
                currency = "5 Y.",
                currencyText = "Term",
                welcomeMiles = "X 1.3",
                welcomeMilesText = "Fayda Max",
                cardImage = "https://abb-bank.az/storage/uploads/files/1735452843_tamkart-premium.webp?v=1053"
            ),
            AllCardInfo(
                title = "Abb Miles Standart",
                price = "FREE",
                priceText = "150 AZN",
                transfer = "2 X. ",
                transferText = "Transfer",
                withDrawal = "FREE",
                withDrawalText = "Cash withdawal",
                tariff = "7%",
                tariffText = "Income on card balance",
                currency = "5 Y.",
                currencyText = "Term",
                welcomeMiles = "X 1.3",
                welcomeMilesText = "Fayda Max",
                cardImage = "https://abb-bank.az/storage/uploads/files/1735452883_abb-miles-standard.webp?v=1053"
            )

        )
    }
}
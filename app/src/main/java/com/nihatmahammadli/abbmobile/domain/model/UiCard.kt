package com.nihatmahammadli.abbmobile.domain.model

data class UiCard(
    val cardNumber: String,
    val expiryDate: String,
    val cvv: String,
    val balance: Double  = 0.0
)

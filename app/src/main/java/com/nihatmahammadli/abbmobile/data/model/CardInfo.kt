    package com.nihatmahammadli.abbmobile.data.model

    data class CardInfo(
        val id: Int = 0,
        val title: String,
        val description: String,
        val backgroundResId: Int,
        val buttonText: String,
        val showVisa: Boolean,
        val cardNumber: String = "",
        val expiryDate: String = "",
        val cvv: String = ""
    )


package com.nihatmahammadli.abbmobile.presentation.model

data class TopUpModel(
    val amount: Double = 0.0,
    val sender: String = "",
    val timeStamp: Long = System.currentTimeMillis(),
    val type: String = "topup",
    val cashBack: Double = 0.0
)

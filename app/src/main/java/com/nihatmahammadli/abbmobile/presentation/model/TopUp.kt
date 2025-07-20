package com.nihatmahammadli.abbmobile.presentation.model

data class TopUp(
    val amount: Double = 0.0,
    val sender: String = "",
    val timeStamp: Long = System.currentTimeMillis(),
    val type: String = "topup"
)

package com.nihatmahammadli.abbmobile.presentation.model

data class TopUp(
    val amount: Int = 0,
    val sender: String = "",
    val timeStamp: Long = System.currentTimeMillis()
)

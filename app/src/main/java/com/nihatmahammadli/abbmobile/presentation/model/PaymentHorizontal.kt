package com.nihatmahammadli.abbmobile.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentHorizontal(
    val imageRes: Int,
    val title: String
): Parcelable

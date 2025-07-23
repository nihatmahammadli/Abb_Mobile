package com.nihatmahammadli.abbmobile.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentVertical(
    var imageRes: Int,
    var title: String
): Parcelable

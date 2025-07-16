package com.nihatmahammadli.abbmobile.presentation.model

sealed class BaseCardData {
    abstract val isCustom: Boolean

    data class DefaultCard(
        val title: String,
        val description: String,
        val backgroundResId: Int,
        val buttonText: String,
        val showVisa: Boolean
    ) : BaseCardData() {
        override val isCustom = false
    }

    data class CustomCard(
        val title: String,
        val balance: String,
        val cardCodeEnding: String,
        val expiryDate: String,
        val backgroundResId: Int,
        val cardLogoResId: Int,
        val showVisa: Boolean = true,
        val onTopUpClick: (() -> Unit)? = null,
        val onPayClick: (() -> Unit)? = null,
        val onTransferClick: (() -> Unit)? = null
    ) : BaseCardData() {
        override val isCustom = true
    }
}

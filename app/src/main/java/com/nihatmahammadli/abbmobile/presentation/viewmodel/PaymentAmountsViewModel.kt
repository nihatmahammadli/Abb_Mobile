package com.nihatmahammadli.abbmobile.presentation.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class PaymentAmountsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _transferResult = MutableLiveData<TransferResult>()
    val transferResult: LiveData<TransferResult> = _transferResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val CASHBACK_PERCENTAGE = 0.02
        private const val MAX_CASHBACK_AMOUNT = 10.0
        private const val MIN_PAYMENT_FOR_CASHBACK = 5.0

        @SuppressLint("DefaultLocale")
        private fun formatAmount(amount: Double): String = "%.2f".format(amount)
    }

    fun transferAmount(amount: Double,
                       paymentType: String = "transfer",
                       applyCashBack: Boolean = true,
                       paymentFor: String? = null) {

        Log.d("PaymentAmountsViewModel", "transferAmount called with:")
        Log.d("PaymentAmountsViewModel", "Amount: $amount")
        Log.d("PaymentAmountsViewModel", "PaymentType: $paymentType")
        Log.d("PaymentAmountsViewModel", "PaymentFor: '$paymentFor'")

        if (amount <= 0) {
            _transferResult.value = TransferResult.Error("Məbləğ müsbət olmalıdır")
            return
        }

        val uid = firebaseAuth.currentUser?.uid
        if (uid == null) {
            _transferResult.value = TransferResult.Error("İstifadəçi daxil olmayıb")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val cardsSnapshot = firestore.collection("users")
                    .document(uid)
                    .collection("cards")
                    .limit(1)
                    .get()
                    .await()

                if (cardsSnapshot.documents.isEmpty()) {
                    _transferResult.value = TransferResult.Error("Kart tapılmadı")
                    _isLoading.value = false
                    return@launch
                }

                val cardId = cardsSnapshot.documents[0].id

                val transactionsSnapshot = firestore.collection("users")
                    .document(uid)
                    .collection("cards")
                    .document(cardId)
                    .collection("transaction")
                    .get()
                    .await()

                val totalBalance = transactionsSnapshot.documents
                    .sumOf { it.getDouble("amount") ?: 0.0 }

                if (totalBalance < amount) {
                    _transferResult.value = TransferResult.Error(
                        "Yetersiz balans! Mövcud balans: ${formatAmount(totalBalance)} AZN"
                    )
                    _isLoading.value = false
                    return@launch
                }

                val roundedAmount = -amount.toRoundedDouble()
                val paymentTransaction = hashMapOf(
                    "amount" to roundedAmount,
                    "timestamp" to System.currentTimeMillis(),
                    "type" to paymentType,
                    "description" to when(paymentType) {
                        "payment" -> "Ödəniş əməliyyatı"
                        "transfer" -> "Transfer əməliyyatı"
                        else -> "Əməliyyat"
                    }
                )

                // PaymentFor sahəsini əlavə et (boş olsa belə)
                if (!paymentFor.isNullOrEmpty()) {
                    paymentTransaction["paymentFor"] = paymentFor
                    Log.d("PaymentAmountsViewModel", "PaymentFor added to transaction: '$paymentFor'")
                } else {
                    Log.d("PaymentAmountsViewModel", "PaymentFor is null or empty")
                }

                firestore.collection("users")
                    .document(uid)
                    .collection("cards")
                    .document(cardId)
                    .collection("transaction")
                    .add(paymentTransaction)
                    .await()

                Log.d("PaymentAmountsViewModel", "Transaction saved to Firebase successfully")

                val cashbackAmount = calculateCashback(amount, paymentType)
                val resultMessage = when(paymentType) {
                    "payment" -> "Ödəniş uğurla tamamlandı"
                    "transfer" -> "Transfer uğurla tamamlandı"
                    else -> "Əməliyyat uğurla tamamlandı"
                }

                if (applyCashBack && cashbackAmount > 0) {
                    val roundedCashback = cashbackAmount.toRoundedDouble()

                    firestore.collection("users")
                        .document(uid)
                        .collection("cards")
                        .document(cardId)
                        .collection("cashbacks")
                        .add(
                            mapOf(
                                "amount" to roundedCashback,
                                "timestamp" to System.currentTimeMillis(),
                                "relatedAmount" to roundedAmount,
                                "relatedType" to paymentType
                            )
                        )
                        .await()
                }

                _transferResult.value = TransferResult.Success(resultMessage)

            } catch (e: Exception) {
                Log.e("PaymentAmountsViewModel", "Error in transferAmount: ${e.message}")
                _transferResult.value = TransferResult.Error("Əməliyyat zamanı xəta baş verdi: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun calculateCashback(amount: Double, paymentType: String): Double {
        val allowedTypes = listOf("payment", "online_shopping", "utilities", "fuel", "transfer")
        if (paymentType !in allowedTypes || amount < MIN_PAYMENT_FOR_CASHBACK) {
            return 0.0
        }
        val cashbackAmount = amount * getCashbackPercentage(paymentType)
        return min(cashbackAmount, MAX_CASHBACK_AMOUNT)
    }

    private fun getCashbackPercentage(paymentType: String): Double {
        return when (paymentType) {
            "payment" -> CASHBACK_PERCENTAGE
            "online_shopping" -> 0.03
            "utilities" -> 0.01
            "fuel" -> 0.05
            "transfer" -> CASHBACK_PERCENTAGE
            else -> 0.0
        }
    }

    fun clearResult() { _transferResult.value = null }

    private fun Double.toRoundedDouble(): Double {
        return "%.2f".format(this).toDouble()
    }
}

sealed class TransferResult {
    data class Success(val message: String) : TransferResult()
    data class Error(val message: String) : TransferResult()
}
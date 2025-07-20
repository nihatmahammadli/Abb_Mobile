package com.nihatmahammadli.abbmobile.presentation.viewmodel

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

@HiltViewModel
class PaymentAmountsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _transferResult = MutableLiveData<TransferResult>()
    val transferResult: LiveData<TransferResult> = _transferResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun transferAmount(amount: Double) {
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
                // Get user's first card
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

                // Get all transactions to calculate balance
                val transactionsSnapshot = firestore.collection("users")
                    .document(uid)
                    .collection("cards")
                    .document(cardId)
                    .collection("transaction")
                    .get()
                    .await()

                val totalBalance = transactionsSnapshot.documents.sumOf {
                    it.getDouble("amount") ?: 0.0
                }

                if (totalBalance < amount) {
                    _transferResult.value = TransferResult.Error("Yetersiz balans! Mövcud balans: ${String.format("%.2f", totalBalance)} AZN")
                    _isLoading.value = false
                    return@launch
                }

                // Create new transaction
                val newTransaction = hashMapOf(
                    "amount" to -amount,
                    "timestamp" to System.currentTimeMillis(),
                    "type" to "transfer",
                    "description" to "Transfer əməliyyatı"
                )

                firestore.collection("users")
                    .document(uid)
                    .collection("cards")
                    .document(cardId)
                    .collection("transaction")
                    .add(newTransaction)
                    .await()

                _transferResult.value = TransferResult.Success("Transfer uğurla tamamlandı")

            } catch (e: Exception) {
                _transferResult.value = TransferResult.Error("Transfer zamanı xəta baş verdi: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearResult() {
        _transferResult.value = null
    }
}

sealed class TransferResult {
    data class Success(val message: String) : TransferResult()
    data class Error(val message: String) : TransferResult()
}
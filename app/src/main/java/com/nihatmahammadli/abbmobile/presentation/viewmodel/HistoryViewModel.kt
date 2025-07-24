package com.nihatmahammadli.abbmobile.presentation.viewmodel

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nihatmahammadli.abbmobile.presentation.model.PaymentSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> = _result

    private val _paymentSum = MutableLiveData<List<PaymentSummary>>()
    val paymentSum: LiveData<List<PaymentSummary>> = _paymentSum

    private val loading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = loading

    private val _totalAmount = MutableLiveData<Double>()
    val totalAmount: LiveData<Double> = _totalAmount


    @SuppressLint("SuspiciousIndentation")
    fun fetchTotalPayments() {
        val uid = firebaseAuth.currentUser?.uid ?: return
        loading.value = true

        viewModelScope.launch {
            try {
                val cardSnapshot = firestore.collection("users")
                    .document(uid)
                    .collection("cards")
                    .get()
                    .await()

                val paymentSumList = mutableListOf<PaymentSummary>()

                for (cardDoc in cardSnapshot) {
                    val cardId = cardDoc.id
                    val paymentTransaction = firestore.collection("users")
                        .document(uid)
                        .collection("cards")
                        .document(cardId)
                        .collection("transaction")
                        .get()
                        .await()

                    for (doc in paymentTransaction.documents) {
                        val type = doc.getString("type")
                        val amount = doc.getDouble("amount") ?: 0.0
                        val paymentFor = doc.getString("paymentFor") ?: "Unknown"

                        if (type == "payment") {
                            val timestampMillis = doc.getLong("timestamp")
                            val formattedDate = timestampMillis?.let {
                                val date = Date(it)
                                val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                format.format(date)
                            } ?: "Naməlum"

                            paymentSumList.add(
                                PaymentSummary(
                                    paymentFor = paymentFor,
                                    totalAmount = kotlin.math.abs(amount),
                                    date = formattedDate
                                )
                            )
                        }
                    }
                }

                paymentSumList.sortByDescending {
                    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(it.date)
                }


                val total = paymentSumList.sumOf { it.totalAmount }
                _totalAmount.value = total
                _paymentSum.value = paymentSumList
                _result.value = true

            } catch (e: Exception) {
                _result.value = false
                e.printStackTrace()
                Log.e("HistoryViewModel", "fetchTotalPayments Error: ${e.message}")
            } finally {
                loading.value = false
            }
        }
    }
}

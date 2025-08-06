package com.nihatmahammadli.abbmobile.presentation.viewmodel

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

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _result = MutableLiveData<Boolean>()
    val result : LiveData<Boolean> = _result

    private val _cashbackTotal = MutableLiveData<Double>()
    val cashbackTotal: LiveData<Double> = _cashbackTotal

    fun fetchTotalCashback() {
        val uid = firebaseAuth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val cardsSnapshot = firestore.collection("users")
                    .document(uid)
                    .collection("cards")
                    .get()
                    .await()

                var totalCashback = 0.0
                for (cardDoc in cardsSnapshot.documents) {
                    val cardId = cardDoc.id
                    val cashbackSnapshot = firestore.collection("users")
                        .document(uid)
                        .collection("cards")
                        .document(cardId)
                        .collection("cashbacks")
                        .get()
                        .await()

                    totalCashback += cashbackSnapshot.documents.sumOf {
                        it.getDouble("amount") ?: 0.0
                    }
                }

                _cashbackTotal.value = totalCashback

            } catch (e: Exception) {
                Log.e("CardViewModel", "Cashback yüklənmədi: ${e.message}")
            }
        }
    }
}
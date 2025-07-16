package com.nihatmahammadli.abbmobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nihatmahammadli.abbmobile.data.model.CardInfo
import com.nihatmahammadli.abbmobile.data.repository.CardRepositoryImpl
import com.nihatmahammadli.abbmobile.domain.model.UiCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel
@Inject constructor(private val repository: CardRepositoryImpl) : ViewModel() {

    private val _cards = MutableLiveData<List<CardInfo>>()
    val cards: LiveData<List<CardInfo>> = _cards

    private val _uiCards = MutableLiveData<List<UiCard>>()
    val uiCards: LiveData<List<UiCard>> = _uiCards

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

     var hasFetchedCards = false

    fun fetchSingleCardFromApi() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val apiCards = repository.getCards()
                val randomCard = apiCards.randomOrNull()

                if (randomCard != null) {
                    _cards.value = listOf(randomCard)
                    saveSingleCardToFirebase(randomCard)

                    _uiCards.value = listOf(
                        UiCard(
                            cardNumber = randomCard.cardNumber,
                            expiryDate = randomCard.expiryDate,
                            cvv = randomCard.cvv
                        )
                    )
                } else {
                    _cards.value = emptyList()
                    _uiCards.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("CardViewModel", "API-dən kart alınmadı: ${e.message}")
//                fetchCardsFromFirebase()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addCardToTop(card: UiCard) {
        val currentList = _uiCards.value?.toMutableList() ?: mutableListOf()
        currentList.add(0, card)
        _uiCards.value = currentList
    }

    private fun saveSingleCardToFirebase(card: CardInfo) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("users")
            .document(userId)
            .collection("cards")
            .get()
            .addOnSuccessListener { snapshot ->
                val batch = firestore.batch()
                snapshot.documents.forEach { doc ->
                    batch.delete(doc.reference)
                }

                val cardMap = hashMapOf(
                    "cardNumber" to card.cardNumber,
                    "expiryDate" to card.expiryDate,
                    "cvv" to card.cvv
                )
                val docRef = firestore.collection("users")
                    .document(userId)
                    .collection("cards")
                    .document()
                batch.set(docRef, cardMap)

                batch.commit()
                    .addOnSuccessListener {
                        Log.d("CardViewModel", "Tək kart Firebase-ə saxlandı")
                    }
                    .addOnFailureListener { e ->
                        Log.e("CardViewModel", "Kart Firebase-ə saxlanmadı: ${e.message}")
                    }
            }
    }

    fun fetchCardsFromFirebase() {
        if (hasFetchedCards) {
            Log.d("CardViewModel", "Firebase çağırılmadı — artıq yüklənib.")
            return
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val firestore = FirebaseFirestore.getInstance()

        _isLoading.value = true

        firestore.collection("users")
            .document(userId)
            .collection("cards")
            .get()
            .addOnSuccessListener { snapshot ->
                val fetchedCards = snapshot.mapNotNull { doc ->
                    val cardNumber = doc.getString("cardNumber") ?: return@mapNotNull null
                    val expiryDate = doc.getString("expiryDate") ?: return@mapNotNull null
                    val cvv = doc.getString("cvv") ?: return@mapNotNull null
                    UiCard(cardNumber, expiryDate, cvv)
                }

                _uiCards.value = fetchedCards
                hasFetchedCards = true  // ✅ Flag burada true olur
                _isLoading.value = false

                Log.d("CardViewModel", "Firebase-dən ${fetchedCards.size} kart yükləndi")
            }
            .addOnFailureListener { e ->
                Log.e("CardFirebase", "Kartlar yüklənmədi: ${e.message}")
                _isLoading.value = false
            }
    }
}

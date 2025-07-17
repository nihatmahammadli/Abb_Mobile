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
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CardViewModel
@Inject constructor(private val repository: CardRepositoryImpl,
                    private val firebaseAuth: FirebaseAuth,
                    private val firestore: FirebaseFirestore) : ViewModel() {

    private val _cards = MutableLiveData<List<CardInfo>>()
    val cards: LiveData<List<CardInfo>> = _cards

    private val _uiCards = MutableLiveData<List<UiCard>>()
    val uiCards: LiveData<List<UiCard>> = _uiCards

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userName = MutableLiveData<String?>()
    val userName: LiveData<String?> get() = _userName

     var hasFetchedCards = false
    var hasFetchedUserName = false

    fun fetchSingleCardFromApi() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val apiCards = repository.getCards()

                val validCard = apiCards.shuffled().firstOrNull { card ->
                    !isCardAlreadyInFirebase(card.cardNumber)
                }

                if (validCard != null) {
                    _cards.value = listOf(validCard)
                    saveSingleCardToFirebase(validCard)

                    _uiCards.value = listOf(
                        UiCard(
                            cardNumber = validCard.cardNumber,
                            expiryDate = validCard.expiryDate,
                            cvv = validCard.cvv
                        )
                    )
                } else {
                    Log.w("CardViewModel", "İstifadə olunmamış kart tapılmadı.")
                    _cards.value = emptyList()
                    _uiCards.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("CardViewModel", "API-dən kart alınmadı: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


    suspend fun isCardAlreadyInFirebase(cardNumber: String): Boolean {
        val snapshot = FirebaseFirestore.getInstance()
            .collection("cards")
            .whereEqualTo("cardNumber", cardNumber)
            .get()
            .await()

        return !snapshot.isEmpty
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

    fun fetchUserNameFromFirebase(){
        if (hasFetchedUserName) return

        val uid =  firebaseAuth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val name = doc.getString("name") ?: "user"
                _userName.value = name
                hasFetchedUserName = true
                Log.d("CardFirebase", "Username $name")
            }.addOnFailureListener { error ->
                _userName.value = "user"
                Log.d("CardFirebase", "Error : $error")
            }
    }
}

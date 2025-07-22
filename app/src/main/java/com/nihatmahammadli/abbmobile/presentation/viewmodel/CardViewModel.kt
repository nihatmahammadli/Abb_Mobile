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

        private val _totalBalance = MutableLiveData<Int>(0)
        val totalBalance: LiveData<Int> = _totalBalance

        private val _cardFetchResult = MutableLiveData<Boolean>()
        val cardFetchResult: LiveData<Boolean> = _cardFetchResult

        private val _cashbackTotal = MutableLiveData<Double>()
        val cashbackTotal: LiveData<Double> = _cashbackTotal

        var hasFetchedCards = false
        var hasFetchedUserName = false

        fun fetchSingleCardFromApi() {
            _isLoading.value = true
            viewModelScope.launch {
                if (isCardAlreadyOrdered()) {
                    _cardFetchResult.value = false
                    _isLoading.value = false
                    Log.w("CardViewModel", "İstifadəçi artıq kart sifariş edib.")
                    return@launch
                }

                try {
                    val apiCards = repository.getCards()
                    val validCard = apiCards.shuffled().firstOrNull { card ->
                        !isCardAlreadyInFirebase(card.cardNumber)
                    }

                    if (validCard != null) {
                        _cardFetchResult.value = true
                        _cards.value = listOf(validCard)
                        saveSingleCardToFirebase(validCard)

                        // 🔥 Firestore-da orderedCard = true qeyd et:
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        uid?.let {
                            FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(it)
                                .update("orderedCard", true)
                        }

                        _uiCards.value = listOf(
                            UiCard(
                                cardNumber = validCard.cardNumber,
                                expiryDate = validCard.expiryDate,
                                cvv = validCard.cvv
                            )
                        )
                    } else {
                        _cards.value = emptyList()
                        _uiCards.value = emptyList()
                        _cardFetchResult.value = false
                    }
                } catch (e: Exception) {
                    Log.e("CardViewModel", "Kart alınarkən xəta: ${e.message}")
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

        suspend fun isCardAlreadyOrdered(): Boolean {
            val uid = firebaseAuth.currentUser?.uid ?: return true
            val doc = firestore.collection("users").document(uid).get().await()

            return doc.getBoolean("orderedCard") ?: false
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

            val userId = firebaseAuth.currentUser?.uid ?: return

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
                    hasFetchedCards = true
                    _isLoading.value = false

                    Log.d("CardViewModel", "Firebase-dən ${fetchedCards.size} kart yükləndi")
                }
                .addOnFailureListener { e ->
                    Log.e("CardFirebase", "Kartlar yüklənmədi: ${e.message}")
                    _isLoading.value = false
                }
        }

        fun fetchTotalCashback() {
            val uid = firebaseAuth.currentUser?.uid ?: return

            viewModelScope.launch {
                try {
                    val snapshot = firestore.collection("users")
                        .document(uid)
                        .collection("cashbacks")
                        .get()
                        .await()

                    val total = snapshot.documents.sumOf {
                        it.getDouble("amount") ?: 0.0
                    }

                    _cashbackTotal.value = total
                } catch (e: Exception) {
                }
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

        fun fetchCardsWithBalances() {
            if (hasFetchedCards) {
                Log.d("CardViewModel", "Firebase çağırılmadı — artıq yüklənib.")
                return
            }

            val userId = firebaseAuth.currentUser?.uid ?: return

            _isLoading.value = true

            viewModelScope.launch {
                try {
                    val cardDocs = firestore.collection("users")
                        .document(userId)
                        .collection("cards")
                        .get()
                        .await()

                    val cardsWithBalances = mutableListOf<UiCard>()

                    for (doc in cardDocs.documents) {
                        val cardNumber = doc.getString("cardNumber") ?: continue
                        val expiryDate = doc.getString("expiryDate") ?: continue
                        val cvv = doc.getString("cvv") ?: continue
                        val cardId = doc.id

                        val transactions = firestore.collection("users")
                            .document(userId)
                            .collection("cards")
                            .document(cardId)
                            .collection("transaction")
                            .get()
                            .await()

                        val balance = transactions.documents.sumOf { it.getDouble("amount") ?: 0.0 }

                        cardsWithBalances.add(
                            UiCard(
                                cardNumber = cardNumber,
                                expiryDate = expiryDate,
                                cvv = cvv,
                                balance = balance
                            )
                        )
                    }

                    _uiCards.postValue(cardsWithBalances)
                    hasFetchedCards = true
                } catch (e: Exception) {
                    Log.e("CardViewModel", "Kartlar və balanslar yüklənmədi: ${e.message}")
                    _uiCards.postValue(emptyList())
                } finally {
                    _isLoading.postValue(false)
                }
            }
        }
    }

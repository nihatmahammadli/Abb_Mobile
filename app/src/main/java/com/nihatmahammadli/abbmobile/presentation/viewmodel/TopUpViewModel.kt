    package com.nihatmahammadli.abbmobile.presentation.viewmodel

    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.firestore.FirebaseFirestore
    import com.nihatmahammadli.abbmobile.presentation.model.TopUpModel
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.tasks.await
    import javax.inject.Inject
    import kotlin.math.roundToInt

    @HiltViewModel
    class TopUpViewModel @Inject constructor(
        private val firebase: FirebaseFirestore,
        private val firebaseAuth: FirebaseAuth
    ): ViewModel() {

        private val _topUpResult = MutableLiveData<TopUpResult>()
        val topUpResult: LiveData<TopUpResult> = _topUpResult

        private val _senderSelected = MutableLiveData<Boolean>(false)
        val senderSelected: LiveData<Boolean> = _senderSelected

        private val _isLoading = MutableLiveData<Boolean>()
        val isLoading: LiveData<Boolean> = _isLoading

        fun setSenderSelected(selected: Boolean) {
            _senderSelected.value = selected
        }

        fun saveAmountInFirebase(amount: Double, sender: String) {
            if (amount <= 0) {
                _topUpResult.value = TopUpResult.Error("Məbləğ müsbət olmalıdır")
                return
            }

            val uid = firebaseAuth.currentUser?.uid
            if (uid == null) {
                _topUpResult.value = TopUpResult.Error("İstifadəçi daxil olmayıb")
                return
            }

            viewModelScope.launch {
                _isLoading.value = true

                try {
                    val cardsSnapshot = firebase.collection("users")
                        .document(uid)
                        .collection("cards")
                        .limit(1)
                        .get()
                        .await()

                    if (cardsSnapshot.documents.isEmpty()) {
                        _topUpResult.value = TopUpResult.Error("Kart tapılmadı")
                        _isLoading.value = false
                        return@launch
                    }

                    val cardId = cardsSnapshot.documents[0].id

                    // Format the amount to 2 decimal places
                    val formattedAmount = "%.2f".format(amount).toDouble()

                    val topUp = TopUpModel(
                        amount = formattedAmount,
                        sender = sender,
                        timeStamp = System.currentTimeMillis(),
                        type = "topup"
                    )

                    firebase.collection("users")
                        .document(uid)
                        .collection("cards")
                        .document(cardId)
                        .collection("transaction")
                        .add(topUp)
                        .await()

                    _topUpResult.value = TopUpResult.Success("Uğurla əlavə olundu!")

                } catch (e: Exception) {
                    _topUpResult.value = TopUpResult.Error("Xəta baş verdi: ${e.message}")
                } finally {
                    _isLoading.value = false
                }
            }
        }

        fun clearResult() {
            _topUpResult.value = null
        }
    }

    sealed class TopUpResult {
        data class Success(val message: String) : TopUpResult()
        data class Error(val message: String) : TopUpResult()
    }
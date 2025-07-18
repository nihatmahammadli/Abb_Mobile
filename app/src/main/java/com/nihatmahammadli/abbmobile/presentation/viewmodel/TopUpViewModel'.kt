package com.nihatmahammadli.abbmobile.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nihatmahammadli.abbmobile.presentation.model.TopUp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopUpViewModel @Inject constructor(
    private val firebase: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): ViewModel() {
    private val _amountText = MutableLiveData<String>("")
    val amountText : LiveData<String> = _amountText

    private val _buttonIsEnabled = MutableLiveData<Boolean>(false)
    val buttonIsEnable : LiveData<Boolean> = _buttonIsEnabled

    private val _topUpResult = MutableLiveData<Boolean>()
    val topUpResult : LiveData<Boolean> = _topUpResult

    private val _senderSelected = MutableLiveData<Boolean>(false)
    val senderSelected: LiveData<Boolean> = _senderSelected

    fun setSenderSelected(selected: Boolean) {
        _senderSelected.value = selected
    }

    fun setAmountText(amount: String) {
        _amountText.value = amount
    }

    fun saveAmountInFirebase(amount: Int, sender: String) {
        val uid = firebaseAuth.currentUser?.uid ?: return

        val topUp = TopUp(
            amount = amount,
            sender = sender,
            timeStamp = System.currentTimeMillis()
        )

        firebase.collection("users")
            .document(uid)
            .collection("cards")
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.documents.isNotEmpty()) {
                    val cardId = documents.documents[0].id

                    firebase.collection("users")
                        .document(uid)
                        .collection("cards")
                        .document(cardId)
                        .collection("transaction")
                        .add(topUp)
                        .addOnSuccessListener {
                            _topUpResult.value = true
                        }.addOnFailureListener {
                            _topUpResult.value = false
                        }
                } else {
                    _topUpResult.value = false
                }
            }
            .addOnFailureListener {
                _topUpResult.value = false
            }
    }

}
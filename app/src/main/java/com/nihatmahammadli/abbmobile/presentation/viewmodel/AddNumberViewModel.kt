package com.nihatmahammadli.abbmobile.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddNumberViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> = _result

    private val _number = MutableLiveData<String>()
    val number: LiveData<String>  = _number

    fun addNumberFirebase() {
        val uid = firebaseAuth.currentUser?.uid
        val phone = _number.value

        if (uid.isNullOrEmpty() || phone.isNullOrEmpty()) {
            _result.value = false
            return
        }

        val userDoc = firestore.collection("users").document(uid)
        val updateMap = mapOf("mobileNumber" to phone)

        userDoc.update(updateMap)
            .addOnSuccessListener {
                _result.value = true
            }
            .addOnFailureListener {
                _result.value = false
            }
    }

    fun fetchNumberFromFirebase(){
        val uid= firebaseAuth.currentUser?.uid ?: return


        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()){
                    _number.value = doc.getString("mobileNumber")
                }else {
                    _number.value = ""
                }
            }.addOnFailureListener {
                _number.value = ""
            }
    }

    fun setNumber(number: String) {
        _number.value = number
    }

}


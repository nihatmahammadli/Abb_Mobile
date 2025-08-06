package com.nihatmahammadli.abbmobile.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore,
): ViewModel() {
    private val _nameInput= MutableLiveData<String>("")
    val nameInput: LiveData<String> = _nameInput

    private val _surNameInput= MutableLiveData<String>("")
    val surNameInput: LiveData<String> = _surNameInput

    private val _isButtonEnabled = MutableLiveData<Boolean>(false)
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    private val _signUpResult = MutableLiveData<Boolean?>()
    val signUpResult: LiveData<Boolean?> = _signUpResult

    fun onFixChangedName(text: String){
        _nameInput.value = text
        updateButtonStateForTexts()
    }

    fun onFixChangedSurname(text: String){
        _surNameInput.value = text
        updateButtonStateForTexts()
    }

    fun updateButtonStateForTexts() {
        val name = nameInput.value.orEmpty()
        val surName = surNameInput.value.orEmpty()
        _isButtonEnabled.value = name.isNotEmpty() && surName.isNotEmpty()
    }

    fun resetResult() {
        _signUpResult.value = null
    }

    fun personalInfo(){
        val name = nameInput.value ?: return
        val surName = surNameInput.value ?: return
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val userMap = hashMapOf(
            "name" to name,
            "surname" to surName
        )

        fireStore.collection("users").document(uid)
            .set(userMap, SetOptions.merge() )
            .addOnCompleteListener {
                _signUpResult.value = true
            }.addOnFailureListener {
                _signUpResult.value = false
            }
    }

}
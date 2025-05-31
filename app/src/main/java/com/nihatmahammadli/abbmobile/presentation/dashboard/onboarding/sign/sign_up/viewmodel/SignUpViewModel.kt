package com.nihatmahammadli.abbmobile.presentation.dashboard.onboarding.sign.sign_up.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
): ViewModel() {

    private val _emailInput = MutableLiveData<String>()
    val emailInput: LiveData<String> = _emailInput

    private val _passwordInput = MutableLiveData<String>()
    val passwordInput: LiveData<String> = _passwordInput

    private val _isButtonEnabled = MutableLiveData<Boolean>(false)
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    private val _signUpResult = MutableLiveData<Boolean>()
    val signUpResult : LiveData<Boolean> = _signUpResult

    fun onFixTextChangedEmail(text: String){
        _emailInput.value = text
        val newText = _emailInput.value
        if (newText != null) {
            _isButtonEnabled.value = text.endsWith("@gmail.com") && text.length > 9 && newText.isEmpty()
        }
    }

    fun singUp(email: String,password: String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val userData = hashMapOf(
                        "email" to email,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    fireStore.collection("users").document(uid)
                        .set(userData)
                        .addOnSuccessListener {
                            _signUpResult.value = task.isSuccessful
                        }
                        .addOnFailureListener {
                            _signUpResult.value = task.isCanceled
                        }
                } else {
                    _signUpResult.value = task.isCanceled
                }
            }
    }

    fun onFixTextChangedPassword(text: String){
        _passwordInput.value = text
        _isButtonEnabled.value = text.length >= 6
    }

}
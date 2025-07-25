package com.nihatmahammadli.abbmobile.presentation.dashboard.onboarding.sign.sign_up.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class SignUpWithFinViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
): ViewModel() {

    private val _finInput = MutableLiveData<String>()
    val finInput : LiveData<String> = _finInput

    private val _isButtonEnabled = MutableLiveData<Boolean>(false)
    val isButtonEnable : LiveData<Boolean> = _isButtonEnabled

    private val _passwordInput = MutableLiveData<String>()
    val passwordInput: LiveData<String> = _passwordInput

    private val _signUpResult = MutableLiveData<Boolean>()
    val signUpResult : LiveData<Boolean> = _signUpResult

    fun onFixTextChangedEmail(text: String){
        _finInput.value = text
        val newText = _passwordInput.value
        if (newText != null) {
            _isButtonEnabled.value = text.length == 7 && newText.isNotEmpty()
        }
    }
    fun onFixTextChangedPassword(text: String){
        _passwordInput.value = text
        _isButtonEnabled.value = text.length >= 6
    }

}
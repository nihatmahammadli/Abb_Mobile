package com.nihatmahammadli.abbmobile.presentation.viewmodel.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
) : ViewModel() {

    private val _emailInput = MutableLiveData<String>("")
    val emailInput: LiveData<String> = _emailInput

    private val _passwordInput = MutableLiveData<String>("")
    val passwordInput: LiveData<String> = _passwordInput

    private val _finInput = MutableLiveData<String>("")
    val finInput: LiveData<String> = _finInput

    private val _isButtonEnabled = MutableLiveData<Boolean>(false)
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    private val _signUpResult = MutableLiveData<Boolean>()
    val signUpResult: LiveData<Boolean> = _signUpResult

    private val _fcmToken = MutableLiveData<String>()
    val fcmToken: LiveData<String> = _fcmToken

    fun fetchFcmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _fcmToken.postValue(task.result) // postValue ilə LiveData dəyişir
                    Log.d("FCM_TOKEN", task.result ?: "Token boşdur")
                } else {
                    Log.e("FCM_TOKEN", "Token alınmadı", task.exception)
                }
            }
    }


    fun updateButtonStateForEmail() {
        val email = emailInput.value.orEmpty()
        val password = passwordInput.value.orEmpty()
        _isButtonEnabled.value = email.endsWith("@gmail.com") && password.length >= 6
    }

    fun updateButtonStateForFin() {
        val fin = finInput.value.orEmpty()
        val password = passwordInput.value.orEmpty()
        val enabled = fin.length == 7 && password.length >= 6
        _isButtonEnabled.value = enabled
    }

    fun onFixTextChangedEmail(text: String) {
        _emailInput.value = text
        updateButtonStateForEmail()
    }

    fun onFixTextChangedPasswordForEmail(text: String) {
        _passwordInput.value = text
        updateButtonStateForEmail()
    }

    fun onFixTextChangedFin(text: String) {
        _finInput.value = text
        updateButtonStateForFin()
    }

    fun onFixTextChangedPasswordForFin(text: String) {
        _passwordInput.value = text
        updateButtonStateForFin()
    }

    fun resetSignUpResult() {
        _signUpResult.value = null
    }

    fun signUp() {
        val email = emailInput.value ?: return
        val password = passwordInput.value ?: return
        val fin = finInput.value ?: ""

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val userData = hashMapOf(
                        "email" to email,
                        "fin" to fin,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    fireStore.collection("users").document(uid)
                        .set(userData)
                        .addOnSuccessListener {
                            _signUpResult.value = true
                            fetchFcmToken()
                        }
                        .addOnFailureListener { _signUpResult.value = false }
                } else {
                    _signUpResult.value = false
                }
            }
    }

    fun signUpWithFin() {
        val fin = finInput.value ?: return
        val password = passwordInput.value ?: return
        val fakeEmail = "$fin@abbmobile.az"

        auth.createUserWithEmailAndPassword(fakeEmail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val userData = hashMapOf(
                        "fin" to fin,
                        "email" to fakeEmail,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                    fireStore.collection("users").document(uid)
                        .set(userData)
                        .addOnSuccessListener { _signUpResult.value = true }
                        .addOnFailureListener { _signUpResult.value = false }
                } else {
                    _signUpResult.value = false
                }
            }
    }


}

package com.nihatmahammadli.abbmobile.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _signInStatus = MutableLiveData<Result<FirebaseUser>>()
    val signInStatus: LiveData<Result<FirebaseUser>> get() = _signInStatus


    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = auth.currentUser
                if (user != null) {
                    _signInStatus.value = Result.success(user)
                } else {
                    _signInStatus.value = Result.failure(Exception("User not found"))
                }
            }.addOnFailureListener { exception ->
                _signInStatus.value = Result.failure(exception)
            }
    }


}
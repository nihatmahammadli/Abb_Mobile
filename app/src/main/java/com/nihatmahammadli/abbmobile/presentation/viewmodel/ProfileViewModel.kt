package com.nihatmahammadli.abbmobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    val name = MutableLiveData<String>()
    val surName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val customerId = MutableLiveData<String>()
    private val _mobileNumber = MutableLiveData<String>()
    val mobileNumber: LiveData<String> = _mobileNumber


    var hasFetchedData = false


    val userFullName = MediatorLiveData<String>().apply {
        addSource(name) { combineNameAndSurname() }
        addSource(surName) { combineNameAndSurname() }
    }

    private fun combineNameAndSurname() {
        val fullName = "${name.value.orEmpty()} ${surName.value.orEmpty()}"
        userFullName.value = fullName.trim()
    }

    fun getCustomerInfoFirebase() {
        val user = firebaseAuth.currentUser ?: return
        val uid = user.uid

        customerId.value = if (uid.length >= 7) uid.substring(0, 7) else uid

        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    name.value = doc.getString("name")
                    surName.value = doc.getString("surname")
                    email.value = doc.getString("email")
                    _mobileNumber.value = doc.getString("mobileNumber")
                }
                hasFetchedData = true
            }
    }


    fun logOut() {
        try {
            firebaseAuth.signOut()
            clearUserData()
            Log.d("ProfileViewModel", "User logged out successfully")

        } catch (e: Exception) {
            Log.e("ProfileViewModel", "Logout failed", e)
        }
    }

    fun observePhoneNumber() {
        val uid = firebaseAuth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                if (snapshot != null && snapshot.exists()) {
                    _mobileNumber.value = snapshot.getString("mobileNumber") ?: ""
                }
            }
    }

    private fun clearUserData() {
        name.value = ""
        surName.value = ""
        email.value = ""
        customerId.value = ""
        _mobileNumber.value = ""
    }

}

package com.nihatmahammadli.abbmobile.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

}
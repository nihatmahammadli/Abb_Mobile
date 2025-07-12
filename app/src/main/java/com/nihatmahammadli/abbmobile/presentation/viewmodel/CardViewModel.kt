package com.nihatmahammadli.abbmobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nihatmahammadli.abbmobile.data.model.CardInfo
import com.nihatmahammadli.abbmobile.domain.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel
   @Inject constructor(private val repository: CardRepository): ViewModel() {
    private val _cards = MutableLiveData<List<CardInfo>>()
    val cards: LiveData<List<CardInfo>> = _cards

    fun fetchCards(){
        viewModelScope.launch {
            try {
                val cardList = repository.getCards()
                _cards.value = cardList
                Log.d("CardViewModel", "Cards $cardList")
            }catch (e: Exception){
                Log.e("CardViewModel", "Error fetching cards", e)
            }
        }
    }
}
package com.nihatmahammadli.abbmobile.presentation.components.sheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.ItemMyCardsSheetBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ItemMyCardSheet @Inject constructor(

): BottomSheetDialogFragment(

) {
    @Inject lateinit var firebaseAuth: FirebaseAuth
    @Inject lateinit var firestore: FirebaseFirestore

    private lateinit var binding: ItemMyCardsSheetBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )!!
            bottomSheet.setBackgroundResource(R.drawable.bottomsheet_background)
        }
        return dialog
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ItemMyCardsSheetBinding.inflate(inflater,container,false)
        binding.myCardSelection.setOnClickListener {
            binding.myCardSelection.isChecked = true
            dismiss()
        }
        setCardNumber()
        return binding.root
    }

    fun setCardNumber() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        firestore.collection("users")
            .document(userId)
            .collection("cards")
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val cardDoc = snapshot.documents.firstOrNull() ?: return@addOnSuccessListener
                val cardNumber = cardDoc.getString("cardNumber") ?: return@addOnSuccessListener

                if (cardNumber.length >= 4) {
                    val last4Digits = cardNumber.takeLast(4)
                    binding.cardNumber.text = "Mastercard · Debit · **** $last4Digits"
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Kart məlumatı tapılmadı", Toast.LENGTH_SHORT).show()
            }
    }


}

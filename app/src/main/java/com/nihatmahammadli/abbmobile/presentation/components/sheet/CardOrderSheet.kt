package com.nihatmahammadli.abbmobile.presentation.components.sheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentCardOrderSheetBinding


class CardOrderSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCardOrderSheetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            ) as View
            bottomSheet.setBackgroundResource(R.drawable.bottomsheet_background)
        }
        return dialog
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCardOrderSheetBinding.inflate(inflater, container, false)
        orderCard()
        return binding.root

    }

    fun orderCard(){
        binding.orderNewCard.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_to_orderNewCard)
            dialog?.dismiss()
        }
    }
    companion object {
        const val TAG = "CardOrderSheet"
    }
}
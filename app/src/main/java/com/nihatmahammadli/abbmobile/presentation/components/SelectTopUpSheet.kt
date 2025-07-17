package com.nihatmahammadli.abbmobile.presentation.components

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.TopUpSheetBinding

class SelectTopUpSheet(
    private val onCardSelected: (String) -> Unit
): BottomSheetDialogFragment() {
    private lateinit var binding: TopUpSheetBinding

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TopUpSheetBinding.inflate(inflater,container,false)

        binding.applyPaySelect.setOnClickListener {
            binding.applyPaySelect.isChecked = true
            binding.customSelect.isChecked = false
            onCardSelected("ApplyPay")
            dismiss()
        }

        binding.customSelect.setOnClickListener {
            binding.applyPaySelect.isChecked = false
            binding.customSelect.isChecked = true
            onCardSelected("CustomCard")
            dismiss()
        }

        return binding.root
    }
}
package com.nihatmahammadli.abbmobile.presentation.components.sheet

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
    private val onCardSelected: (String) -> Unit,
    private var selectedOption: String? = null
) : BottomSheetDialogFragment() {

    private lateinit var binding: TopUpSheetBinding

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
        binding = TopUpSheetBinding.inflate(inflater, container, false)

        when (selectedOption) {
            "ApplyPay" -> {
                binding.applyPaySelect.isChecked = true
                binding.customSelect.isChecked = false
            }
            "CustomCard" -> {
                binding.applyPaySelect.isChecked = false
                binding.customSelect.isChecked = true
            }
            else -> {
                binding.applyPaySelect.isChecked = false
                binding.customSelect.isChecked = false
            }
        }

        binding.applyPaySelect.setOnClickListener {
            selectedOption = "ApplyPay"
            onCardSelected("ApplyPay")
            dismiss()
        }

        binding.customSelect.setOnClickListener {
            selectedOption = "CustomCard"
            onCardSelected("CustomCard")
            dismiss()
        }

        return binding.root
    }

}

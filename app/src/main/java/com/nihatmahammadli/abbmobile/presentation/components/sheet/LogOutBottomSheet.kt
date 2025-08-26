package com.nihatmahammadli.abbmobile.presentation.components.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nihatmahammadli.abbmobile.databinding.LogOutBottomSheetBinding

class LogOutBottomSheet(
    private val onConfirm: () -> Unit
): BottomSheetDialogFragment() {
    private lateinit var binding: LogOutBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LogOutBottomSheetBinding.inflate(inflater, container, false)

        binding.btnLogOut.setOnClickListener {
            dismiss()
            onConfirm()
        }
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}
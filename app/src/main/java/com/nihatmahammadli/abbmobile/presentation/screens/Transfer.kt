package com.nihatmahammadli.abbmobile.presentation.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.databinding.FragmentTransferBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Transfer : Fragment() {
    private lateinit var binding: FragmentTransferBinding

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {}

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) openCamera()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransferBinding.inflate(inflater, container, false)


        binding.transferBtn.setOnClickListener {
            val cardNumber = binding.cardNumber.text.toString().replace(" ", "")

            val action = TransferDirections.actionTransferToPaymentsAmount(
                fromTransferPage = true,
                paymentVertical = null,
                paymentHorizontal = null,
                cardNumber = cardNumber
            )
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cameraBtn.setOnClickListener {
            checkCameraPermissionAndOpen()
        }

        goBack()
        formatCardNumber(binding.cardNumber)
    }

    private fun checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun goBack() {
        binding.leftBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun formatCardNumber(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            var isFormatting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isFormatting) return
                val digitsOnly = s.toString().replace(" ", "")
                binding.transferBtn.visibility =
                    if (digitsOnly.length == 16) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                val digitsOnly = s.toString().replace(" ", "")
                val formatted = StringBuilder()
                for ((index, c) in digitsOnly.withIndex()) {
                    formatted.append(c)
                    if ((index + 1) % 4 == 0 && index + 1 != digitsOnly.length) {
                        formatted.append(" ")
                    }
                }

                val newText = formatted.toString()
                if (newText != s.toString()) {
                    editText.setText(newText)
                    editText.setSelection(newText.length)
                }

                isFormatting = false
            }
        })
    }
}

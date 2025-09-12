package com.nihatmahammadli.abbmobile.presentation.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.databinding.FragmentAddNumberBinding
import com.nihatmahammadli.abbmobile.presentation.viewmodel.AddNumberViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNumber : Fragment() {
    private lateinit var binding: FragmentAddNumberBinding
    private val viewModel: AddNumberViewModel by viewModels()
    private var firebaseNumber: String? = null
    private val prefix = "+994"

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            val currentInput = s?.toString()
            if (currentInput == firebaseNumber && currentInput?.length == 13) {
                binding.mobileNumberLayout.suffixText = "Activate"
            } else {
                binding.mobileNumberLayout.suffixText = null
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNumberBinding.inflate(inflater, container, false)
        setUpPrefixControl()
        setUpObservers()
        return binding.root
    }

    private fun setUpPrefixControl() {
        val editText = binding.mobileText

        editText.setText(prefix)
        editText.setSelection(prefix.length)

        editText.addTextChangedListener(object : TextWatcher {
            var isUpdating = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                s?.let {
                    if (!it.toString().startsWith(prefix)) {
                        isUpdating = true
                        editText.setText(prefix)
                        editText.setSelection(prefix.length)
                        isUpdating = false
                    }
                }
            }
        })

        editText.addTextChangedListener(textWatcher)
    }

    private fun setUpObservers() {
        viewModel.number.observe(viewLifecycleOwner) { number ->
            firebaseNumber = number

            binding.mobileText.removeTextChangedListener(textWatcher)

            if (!number.isNullOrEmpty()) {
                binding.mobileText.setText(number)
                binding.mobileText.setSelection(number.length)
                binding.mobileNumberLayout.suffixText = "Activate"
            } else {
                binding.mobileText.setText("")
                binding.mobileNumberLayout.suffixText = null
            }

            binding.mobileText.addTextChangedListener(textWatcher)

        }

        binding.mobileNumberLayout.setEndIconOnClickListener {
            val currentInput = binding.mobileText.text.toString()
            if (currentInput == firebaseNumber && currentInput.length == 13) {
                viewModel.setNumber(currentInput)
                viewModel.addNumberFirebase()
            }
        }
        binding.btnContinue.setOnClickListener {
            val mobileNumber = binding.mobileText.text.toString()
            if (mobileNumber.length == 13 && mobileNumber.startsWith(prefix)) {
                viewModel.setNumber(mobileNumber)
                viewModel.addNumberFirebase()
            } else {
                Toast.makeText(requireContext(), "Nömrə düzgün deyil", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.result.observe(viewLifecycleOwner) { success ->
            val msg = if (success) "Mobil nömrə əlavə olundu" else "Xəta baş verdi"
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
        viewModel.fetchNumberFromFirebase()
    }
}

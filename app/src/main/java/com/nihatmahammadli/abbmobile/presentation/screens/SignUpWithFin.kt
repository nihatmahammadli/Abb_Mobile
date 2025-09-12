package com.nihatmahammadli.abbmobile.presentation.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentSignUpWithFinBinding
import com.nihatmahammadli.abbmobile.presentation.viewmodel.viewmodel.SignUpViewModel

class SignUpWithFin : Fragment() {

    private lateinit var binding: FragmentSignUpWithFinBinding
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpWithFinBinding.inflate(inflater, container, false)
        setupListeners()
        observeViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.signUpResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                findNavController().navigate(R.id.action_signUpWithFin_to_userInfo)
            } else {
                Toast.makeText(requireContext(), "Sign up uğursuz oldu.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupListeners() {
        binding.finText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onFixTextChangedFin(s.toString().trim())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.passwordText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onFixTextChangedPasswordForFin(s.toString().trim())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnContinue.setOnClickListener {
            viewModel.signUpWithFin()
        }

        binding.leftBtn.setOnClickListener {
            binding.finText.text?.clear()
            binding.passwordText.text?.clear()
            findNavController().popBackStack()
        }
    }

    private fun observeViewModel() {
        viewModel.finInput.observe(viewLifecycleOwner) { text ->
            if (binding.finText.text.toString() != text) {
                binding.finText.setText(text)
                binding.finText.setSelection(text.length)
            }
        }

        viewModel.passwordInput.observe(viewLifecycleOwner) { text ->
            if (binding.passwordText.text.toString() != text) {
                binding.passwordText.setText(text)
                binding.passwordText.setSelection(text.length)
            }
        }

        viewModel.isButtonEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.btnContinue.isEnabled = enabled
            binding.btnContinue.alpha = if (enabled) 1f else 0.5f
        }
    }
}

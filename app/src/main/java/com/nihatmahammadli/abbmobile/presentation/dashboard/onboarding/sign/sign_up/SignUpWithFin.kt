package com.nihatmahammadli.abbmobile.presentation.dashboard.onboarding.sign.sign_up

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentSignUpWithFinBinding
import com.nihatmahammadli.abbmobile.presentation.dashboard.onboarding.sign.sign_up.viewmodel.SignUpWithFinViewModel

class SignUpWithFin : Fragment() {
    private lateinit var binding: FragmentSignUpWithFinBinding
    private val viewModel: SignUpWithFinViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpWithFinBinding.inflate(inflater,container,false)

        observeViewModel()
        bindFinInputListenerEmail()
        bindFinInputListenerPassword()
        goDatePicker()
        goBack()
        return binding.root
    }

    fun observeViewModel(){
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

        viewModel.isButtonEnable.observe(viewLifecycleOwner) { enabled ->
            binding.btnContinue.isEnabled = enabled
            binding.btnContinue.alpha = if (enabled) 1f else 0.5f
        }
    }

    private fun bindFinInputListenerEmail(){
        binding.finText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.onFixTextChangedEmail(s.toString().trim())
            }
        })
    }

    private fun bindFinInputListenerPassword(){
        binding.passwordText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.onFixTextChangedPassword(s.toString().trim())
            }
        })
    }

    fun goDatePicker(){
        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_signUpWithFin_to_enterDateOfBirth)
        }
    }

    fun goBack(){
        binding.leftBtn.setOnClickListener {
            binding.finText.text?.clear()
            binding.passwordText.text?.clear()
            findNavController().popBackStack()
        }

    }
}
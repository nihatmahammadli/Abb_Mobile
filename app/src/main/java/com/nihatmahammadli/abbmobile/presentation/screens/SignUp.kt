package com.nihatmahammadli.abbmobile.presentation.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentSignUpBinding
import com.nihatmahammadli.abbmobile.presentation.viewmodel.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUp : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        signUpViewModel.resetSignUpResult()

        setupListeners()
        observeViewModel()
        observeSignUpResult()
        return binding.root
    }

    private fun setupListeners() {
        binding.emailText.addTextChangedListener(SimpleTextWatcher {
            signUpViewModel.onFixTextChangedEmail(it.trim())
        })
        binding.passwordText.addTextChangedListener(SimpleTextWatcher {
            signUpViewModel.onFixTextChangedPasswordForEmail(it.trim())
        })

        binding.btnContinue.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            signUpViewModel.signUp()
        }

        binding.leftBtn.setOnClickListener {
            binding.emailText.text?.clear()
            binding.passwordText.text?.clear()
            findNavController().popBackStack()
        }

        binding.signUpWithFin.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_signUpWithFin)
        }
    }

    private fun observeViewModel() {
        signUpViewModel.isButtonEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.btnContinue.isEnabled = enabled
            binding.btnContinue.alpha = if (enabled) 1f else 0.5f
        }
    }


    private fun observeSignUpResult() {
        signUpViewModel.signUpResult.observe(viewLifecycleOwner) { success ->
            success?.let {

                if (it) {
                    binding.progressBar.visibility = View.GONE

                    Toast.makeText(requireContext(), "Sign up uğurlu oldu.", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signUp_to_userInfo)
                } else {
                    binding.progressBar.visibility = View.GONE

                    Toast.makeText(requireContext(), "Sign up uğursuz oldu.", Toast.LENGTH_SHORT).show()
                }
                signUpViewModel.resetSignUpResult()
            }
        }
    }

}

class SimpleTextWatcher(private val onAfterTextChanged: (String) -> Unit) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        onAfterTextChanged(s.toString())
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}
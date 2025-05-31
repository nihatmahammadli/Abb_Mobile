package com.nihatmahammadli.abbmobile.presentation.dashboard.onboarding.sign.sign_up

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentSignUpBinding
import com.nihatmahammadli.abbmobile.presentation.dashboard.onboarding.sign.sign_up.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUp : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel

    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        signUpViewModel = SignUpViewModel(auth,firestore)

        observerViewModel()
        singUp()
        bindInputListenerEmail()
        bindInputListenerPassword()
        goCustomerTypeSelection()

        goSignUpWithFin()
        return binding.root
    }



    fun goCustomerTypeSelection(){
        binding.leftBtn.setOnClickListener {
            binding.passwordText.text?.clear()
            binding.emailText.text?.clear()
            findNavController().popBackStack()
        }
    }

    fun goDatePick(){
       findNavController().navigate(R.id.action_signUp_to_enterDateOfBirth)
    }

    fun goSignUpWithFin(){
        binding.signUpWithFin.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_signUpWithFin)
        }
    }

    fun observerViewModel(){
        signUpViewModel.emailInput.observe(viewLifecycleOwner) { text ->
            if (binding.emailText.text.toString() != text){
                binding.emailText.setText(text)
                binding.emailText.setSelection(text.length)
            }
        }
        signUpViewModel.passwordInput.observe(viewLifecycleOwner) { text ->
            if (binding.passwordText.text.toString() != text){
                binding.passwordText.setText(text)
                binding.passwordText.setSelection(text.length)
            }
        }
        signUpViewModel.isButtonEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.btnContinue.isEnabled = enabled
            binding.btnContinue.alpha = if (enabled) 1f else 0.5f
        }
    }

    fun bindInputListenerEmail(){
        binding.emailText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                signUpViewModel.onFixTextChangedEmail(s.toString().trim())
            }
        })
    }

    fun bindInputListenerPassword(){
        binding.passwordText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                signUpViewModel.onFixTextChangedPassword(s.toString().trim())
            }
        })
    }

    fun singUp(){
        binding.btnContinue.setOnClickListener {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()
        signUpViewModel.singUp(email,password)

        signUpViewModel.signUpResult.observe(viewLifecycleOwner) { success ->
            if(success) {
                goDatePick()
            } else {
                Toast.makeText(requireContext(), "Qeydiyyat uğurlu deyildir!", Toast.LENGTH_SHORT).show()
            }
        }
        }
    }

}
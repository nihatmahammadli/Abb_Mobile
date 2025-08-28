package com.nihatmahammadli.abbmobile.presentation.screens

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentSignInBinding
import com.nihatmahammadli.abbmobile.presentation.components.ui.SimpleTextWatcher
import com.nihatmahammadli.abbmobile.presentation.viewmodel.SignInViewModel
import com.nihatmahammadli.abbmobile.presentation.viewmodel.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignIn : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SignInViewModel by viewModels()
    private val signUpViewModel: SignUpViewModel by viewModels()

    @Inject
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater,container,false)
        signInWithEmail()
        signIn()
        setUpListeners()
        observeViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


     fun setUpListeners(){
        binding.emailText.addTextChangedListener(SimpleTextWatcher{
            signUpViewModel.onFixTextChangedEmail(it.trim())
        })
        binding.passwordText.addTextChangedListener(SimpleTextWatcher{
            signUpViewModel.onFixTextChangedPasswordForEmail(it.trim())
        })
        binding.leftBtn.setOnClickListener {
            binding.emailText.text?.clear()
            binding.passwordText.text?.clear()
            findNavController().popBackStack()
        }
    }

    private fun observeViewModel(){
        signUpViewModel.isButtonEnabled.observe(viewLifecycleOwner) {enabled ->
            binding.btnContinue.isEnabled = enabled
            binding.btnContinue.alpha = if (enabled) 1f else 0.5f
        }
    }



    fun signIn(){
        viewModel.signInStatus.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                binding.progressBar.visibility = View.GONE

                val sharedPref = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                sharedPref.edit { putBoolean("isLoggedIn", true) }

                findNavController().navigate(R.id.action_signIn_to_homePage)
            }
            result.onFailure {
                binding.progressBar.visibility = View.GONE

                Toast.makeText(requireContext(), "User not found!", Toast.LENGTH_SHORT).show()
            }
        }
    }




    fun signInWithEmail() {
        binding.btnContinue.setOnClickListener {
            val email = binding.emailText.text.toString().trim()
            val password = binding.passwordText.text.toString().trim()


            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Email and password cannot be empty!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            Log.d("TEST", "ProgressBar exists: visible")

            binding.progressBar.visibility = View.VISIBLE
            viewModel.signIn(email, password)
        }
    }
}

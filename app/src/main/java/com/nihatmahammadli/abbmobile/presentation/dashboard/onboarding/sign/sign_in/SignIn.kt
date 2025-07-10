package com.nihatmahammadli.abbmobile.presentation.dashboard.onboarding.sign.sign_in

import android.content.Context
import android.os.Bundle
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
import com.nihatmahammadli.abbmobile.presentation.dashboard.onboarding.sign.sign_in.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignIn : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SignInViewModel by viewModels()

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInWithEmail()
        goBack()

        viewModel.signInStatus.observe(viewLifecycleOwner) { result ->
            result.onSuccess {

                val sharedPref = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                sharedPref.edit { putBoolean("isLoggedIn", true) }

                findNavController().navigate(R.id.action_signIn_to_homePage)
            }
            result.onFailure {
                Toast.makeText(requireContext(), "User not found!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun goBack(){
        binding.leftBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun signInWithEmail() {
        binding.btnContinue.setOnClickListener {
            val email = binding.emailText.text.toString().trim()
            val password = binding.passwordText.text.toString().trim()



            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Email and password cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.signIn(email, password)
        }
    }


}
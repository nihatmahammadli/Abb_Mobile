package com.nihatmahammadli.abbmobile.presentation.dashboard.onboarding.sign.user_info

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
import com.nihatmahammadli.abbmobile.databinding.FragmentUserInfoBinding
import com.nihatmahammadli.abbmobile.presentation.dashboard.onboarding.sign.user_info.UserInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfo : Fragment() {

    private lateinit var binding: FragmentUserInfoBinding
    private val userInfoViewModel: UserInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)

        userInfoViewModel.resetResult()

        setUpListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun setUpListeners(){
        binding.nameText.addTextChangedListener(SimpleTextWatcher {
            userInfoViewModel.onFixChangedName(it.trim())
        })

        binding.surnameText.addTextChangedListener(SimpleTextWatcher {
            userInfoViewModel.onFixChangedSurname(it.trim())
        })

        binding.leftBtn.setOnClickListener {
            binding.nameText.text?.clear()
            binding.surnameText.text?.clear()
            findNavController().popBackStack()
        }

        binding.btnContinue.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            userInfoViewModel.personalInfo()
        }
    }

    private fun observeViewModel() {
        userInfoViewModel.isButtonEnabled.observe(viewLifecycleOwner) { enabled ->
            binding.btnContinue.isEnabled = enabled
            binding.btnContinue.alpha = if (enabled) 1f else 0.5f
        }

        userInfoViewModel.signUpResult.observe(viewLifecycleOwner) { success ->
            success?.let {
                if (it) {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_userInfo_to_enterDateOfBirth)
                } else {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Failed to save personal info", Toast.LENGTH_SHORT).show()
                }
                userInfoViewModel.resetResult()
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
}
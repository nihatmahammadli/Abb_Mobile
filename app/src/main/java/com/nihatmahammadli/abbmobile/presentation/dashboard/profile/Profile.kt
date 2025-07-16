package com.nihatmahammadli.abbmobile.presentation.dashboard.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentProfileBinding
import com.nihatmahammadli.abbmobile.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Profile : Fragment() {
   private lateinit var binding: FragmentProfileBinding
   private val profileViewModel: ProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    fun initUI(){
        goBack()
        setUserInfos()
    }

    fun goBack(){
        binding.leftBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    fun setUserInfos() {
        profileViewModel.userFullName.observe(viewLifecycleOwner) { fullName ->
            binding.userName.text = fullName
        }
        profileViewModel.email.observe(viewLifecycleOwner) {

            binding.emailText.text = profileViewModel.email.value
        }

        profileViewModel.customerId.observe(viewLifecycleOwner){
            binding.customerIdText.text = "Customer ID - ${profileViewModel.customerId.value}"
        }
        profileViewModel.getCustomerInfoFirebase()
    }



}
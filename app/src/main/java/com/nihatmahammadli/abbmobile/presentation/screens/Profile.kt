package com.nihatmahammadli.abbmobile.presentation.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.MainActivity
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentProfileBinding
import com.nihatmahammadli.abbmobile.presentation.components.sheet.LogOutBottomSheet
import com.nihatmahammadli.abbmobile.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Profile : Fragment() {
   private lateinit var binding: FragmentProfileBinding
   private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        initUI()

        if (!profileViewModel.hasFetchedData) {
            profileViewModel.getCustomerInfoFirebase()
        }
        profileViewModel.observePhoneNumber()

        return binding.root
    }

    fun initUI(){
        binding.logOut.setOnClickListener {
            showLogOutBottomSheet()
        }
        goBack()
        setUserInfos()
        copyText()
        goAddNumber()
        setUpSwipeToRefresh()
    }

    private fun setUpSwipeToRefresh(){
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun refreshData(){
        profileViewModel.hasFetchedData = false
        profileViewModel.getCustomerInfoFirebase()
        binding.swipeRefreshLayout.isRefreshing = false
    }


    fun goAddNumber(){
        binding.arrowMobile.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_addNumber)
        }
        binding.mobileLinear.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_addNumber)
        }

        binding.mobileNumIc.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_addNumber)
        }
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
        profileViewModel.mobileNumber.observe(viewLifecycleOwner){
            binding.mobileNumber.text = profileViewModel.mobileNumber.value
        }
    }


    private fun showLogOutBottomSheet() {
        val bottomSheet = LogOutBottomSheet {
            performLogOut()
        }
        bottomSheet.show(parentFragmentManager, "LogOutBottomSheet")
    }

    private fun performLogOut() {
        profileViewModel.logOut()

        val sharedPref = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPref.edit { putBoolean("isLoggedIn", false) }

        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

//    L7Z0of7
    fun copyText(){
        binding.customerIdText.setOnClickListener {
            val fullText = binding.customerIdText.text.toString()
            val toCopy = if (fullText.length >= 7) fullText.takeLast(7) else fullText

            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("copied_text", toCopy)
            clipboard.setPrimaryClip(clip)
        }
        }

}
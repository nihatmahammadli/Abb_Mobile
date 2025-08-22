package com.nihatmahammadli.abbmobile.presentation.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentMenuBinding
import com.nihatmahammadli.abbmobile.presentation.components.ui.LocaleHelper

class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        initUi()

        return binding.root
    }

    fun initUi(){
        callClick()
        changeLanguageAz()
        changeLanguageEn()
        changeLanguageRu()
        hideSwipe()
        goServiceNetwork()
        goContact()
    }

    @SuppressLint("UseKtx")
    fun callClick(){
        val number = "937"
        binding.callUs.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$number")
            startActivity(intent)
        }
    }

    fun changeLanguageAz() {
        binding.changeAze.setOnClickListener {
            LocaleHelper.saveLanguage(requireContext(), "az")
            requireActivity().recreate()
        }
    }

    fun changeLanguageEn(){
        binding.changeEn.setOnClickListener {
            LocaleHelper.saveLanguage(requireContext(), "en")
            requireActivity().recreate()
        }
    }

    fun changeLanguageRu(){
        binding.changeRu.setOnClickListener {
            LocaleHelper.saveLanguage(requireContext(), "ru")
            requireActivity().recreate()
        }
    }

    fun showSwipeDown() {
        binding.arrow.visibility = View.VISIBLE
        binding.textView4.visibility = View.VISIBLE
        binding.textView4.text = getString(R.string.swipe_for_menu)
    }

    fun hideSwipe() {
        binding.arrow.visibility = View.GONE
        binding.textView4.visibility = View.GONE
    }

    fun goServiceNetwork(){
        binding.serviceNetwork.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_serviceNetwork)
        }
    }

    fun goContact(){
        binding.contact.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_contact)
        }
    }
}
package com.nihatmahammadli.abbmobile.presentation.dashboard.onboarding

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nihatmahammadli.abbmobile.databinding.FragmentMenuBinding
import com.nihatmahammadli.abbmobile.presentation.dashboard.ui.LocaleHelper

class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(inflater,container,false)
        callClick()
        changeLanguageAz()
        changeLanguageEn()
        changeLanguageRu()
        return binding.root
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


}
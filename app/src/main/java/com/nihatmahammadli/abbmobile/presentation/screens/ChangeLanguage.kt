package com.nihatmahammadli.abbmobile.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentChangeLanguageBinding
import com.nihatmahammadli.abbmobile.presentation.components.ui.LocaleHelper

class ChangeLanguage : Fragment() {
    private lateinit var binding: FragmentChangeLanguageBinding
    private lateinit var radios: List<RadioButton>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeLanguageBinding.inflate(inflater, container, false)

        radios = listOf(binding.radioAz, binding.radioEn, binding.radioRu)

        setupBack()
        restoreSelection()
        setupClicks()

        return binding.root
    }

    private fun setupBack() {
        binding.leftBtn.setOnClickListener { findNavController().navigateUp() }
    }

    private fun restoreSelection() {
        when (LocaleHelper.getSavedLanguage(requireContext())) {
            "az" -> binding.radioAz.isChecked = true
            "en" -> binding.radioEn.isChecked = true
            "ru" -> binding.radioRu.isChecked = true
        }
    }

    private fun setupClicks() {
        binding.radioAz.setOnClickListener { select(binding.radioAz, "az") }
        binding.radioEn.setOnClickListener { select(binding.radioEn, "en") }
        binding.radioRu.setOnClickListener { select(binding.radioRu, "ru") }
    }

    private fun select(chosen: RadioButton, lang: String) {
        radios.forEach { it.isChecked = (it == chosen) }
        LocaleHelper.saveLanguage(requireContext(), lang)
        requireActivity().recreate()
    }
}

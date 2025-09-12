package com.nihatmahammadli.abbmobile.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.databinding.FragmentContactBinding

class Contact : Fragment() {
    private lateinit var binding: FragmentContactBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactBinding.inflate(inflater, container, false)

        callClick()
        goBack()
        return binding.root
    }

    fun callClick() {
        val number = "937"
        binding.callUs.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = "tel:$number".toUri()
            startActivity(intent)
        }
    }

    fun goBack() {
        binding.leftBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}
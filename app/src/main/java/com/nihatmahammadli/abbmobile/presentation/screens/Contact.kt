package com.nihatmahammadli.abbmobile.presentation.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentContactBinding
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController

class Contact : Fragment() {
    private lateinit var binding: FragmentContactBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactBinding.inflate(inflater, container, false)

        callClick()
        goBack()
        return binding.root
    }

    fun callClick(){
        val number = "937"
        binding.callUs.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = "tel:$number".toUri()
            startActivity(intent)
        }
    }

    fun goBack(){
        binding.leftBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}
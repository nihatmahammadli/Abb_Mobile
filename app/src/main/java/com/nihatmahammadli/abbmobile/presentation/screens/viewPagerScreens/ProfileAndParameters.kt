package com.nihatmahammadli.abbmobile.presentation.screens.viewPagerScreens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentProfileAndParametersBinding

class ProfileAndParameters : Fragment() {
    private lateinit var binding: FragmentProfileAndParametersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileAndParametersBinding.inflate(inflater,container,false)
        return binding.root
    } 



}
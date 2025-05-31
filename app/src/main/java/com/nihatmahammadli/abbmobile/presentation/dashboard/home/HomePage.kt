package com.nihatmahammadli.abbmobile.presentation.dashboard.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nihatmahammadli.abbmobile.databinding.FragmentHomePageBinding

class HomePage : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater,container,false)
        return binding.root
    }
}
package com.nihatmahammadli.abbmobile.presentation.dashboard.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nihatmahammadli.abbmobile.databinding.FragmentHomePageBinding
import androidx.core.content.edit
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.nihatmahammadli.abbmobile.R

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.exitBtn.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            sharedPref.edit { putBoolean("isLoggedIn", false) }
            findNavController().navigate(R.id.action_homePage_to_mainFragment)
        }
    }



}

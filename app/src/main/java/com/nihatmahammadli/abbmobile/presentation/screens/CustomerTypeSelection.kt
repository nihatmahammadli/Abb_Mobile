package com.nihatmahammadli.abbmobile.presentation.screens

import android.annotation.SuppressLint

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentCustomerTypeSelectionBinding



class CustomerTypeSelection : Fragment() {
    private var _binding: FragmentCustomerTypeSelectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomerTypeSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goSignUp()
        goSignIn()
    }

    fun goSignUp(){
        binding.notAnAbbCustomer.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_signUp)
        }
    }

    fun goSignIn(){
        binding.abbCustomer.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_signIn)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




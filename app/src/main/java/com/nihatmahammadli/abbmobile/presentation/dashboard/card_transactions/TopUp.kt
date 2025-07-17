package com.nihatmahammadli.abbmobile.presentation.dashboard.card_transactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentTopUpBinding
import com.nihatmahammadli.abbmobile.presentation.components.SelectTopUpSheet

class TopUp : Fragment() {
    private lateinit var binding: FragmentTopUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopUpBinding.inflate(inflater,container,false)
        goBack()
        showTopUpSheet()
        return binding.root
    }

    fun goBack(){
        binding.leftBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showTopUpSheet(){
        binding.senderBtn.setOnClickListener {
            SelectTopUpSheet { selectedCard ->
                Toast.makeText(requireContext(), "Seçildi: $selectedCard", Toast.LENGTH_SHORT).show()
                if (selectedCard == "ApplyPay") {
                    val newIcon = ContextCompat.getDrawable(requireContext(), R.drawable.apple_pay_ic)
                    binding.senderBtn.setCompoundDrawablesWithIntrinsicBounds(newIcon, null, null, null)
                }else {

                }
            }.show(parentFragmentManager, "SelectTopUpSheet")

        }
    }

}
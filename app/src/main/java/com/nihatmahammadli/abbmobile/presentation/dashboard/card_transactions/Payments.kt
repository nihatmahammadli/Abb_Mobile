package com.nihatmahammadli.abbmobile.presentation.dashboard.card_transactions

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentPaymentsBinding
import com.nihatmahammadli.abbmobile.presentation.adapters.PaymentHorizontalAdapter
import com.nihatmahammadli.abbmobile.presentation.adapters.PaymentVerticalAdapter
import com.nihatmahammadli.abbmobile.presentation.components.PaymentDummyData


class Payments : Fragment() {
    private lateinit var binding: FragmentPaymentsBinding
    private lateinit var horizontalRcy: PaymentHorizontalAdapter
    private lateinit var verticalRcy: PaymentVerticalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentsBinding.inflate(inflater,container,false)
        setUpRecyclerView()
        setUpVerticalRcy()
        return binding.root
    }
    fun setUpRecyclerView() {
        horizontalRcy = PaymentHorizontalAdapter(PaymentDummyData.horizontalList) {
            findNavController().navigate(R.id.action_payments_to_paymentsAmount)
        }
        binding.recyclerViewHorizontal.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = horizontalRcy
        }
    }

    fun setUpVerticalRcy() {
        verticalRcy = PaymentVerticalAdapter(PaymentDummyData.verticalList){
            findNavController().navigate(R.id.action_payments_to_paymentsAmount)
        }
        binding.recyclerViewVertical.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = verticalRcy
        }
    }



}
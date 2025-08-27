package com.nihatmahammadli.abbmobile.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nihatmahammadli.abbmobile.databinding.FragmentHistoryBinding
import com.nihatmahammadli.abbmobile.presentation.adapters.HistoryFilterAdapter
import com.nihatmahammadli.abbmobile.presentation.adapters.HistoryPaymentsAdapter
import com.nihatmahammadli.abbmobile.presentation.model.PaymentSummary
import com.nihatmahammadli.abbmobile.presentation.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class History : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var filterAdapter: HistoryFilterAdapter
    private lateinit var historyAdapter: HistoryPaymentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        fetchTotalPayments()
        setObservers()
        setUpAdapters()
        return binding.root
    }

    private fun fetchTotalPayments() {
        viewModel.fetchTotalPayments()
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        viewModel.totalAmount.observe(viewLifecycleOwner) { total ->
            binding.amount.text = "%.2f ₼".format(total)

            val progressValue = if (total > 400) 400 else total.toInt()
            binding.progressBar2.progress = progressValue
        }

        viewModel.paymentSum.observe(viewLifecycleOwner) { list ->
            val sortedList = list.sortedByDescending {
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(it.date)
            }
            if (list.isNullOrEmpty()){
                binding.historyRecyclerView.visibility = View.GONE
                binding.emptyIcon.visibility = View.VISIBLE
                binding.emptyText.visibility = View.VISIBLE
            }else {
                historyAdapter = HistoryPaymentsAdapter(sortedList)
                binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.historyRecyclerView.adapter = historyAdapter

                binding.historyRecyclerView.visibility = View.VISIBLE
                binding.emptyIcon.visibility = View.GONE
                binding.emptyText.visibility = View.GONE
            }
        }
    }

    private fun setUpAdapters() {
        val buttonList = listOf("Direction", "Period", "Type", "Cards", "Calculation")

        filterAdapter = HistoryFilterAdapter(buttonList)
        binding.rcyView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcyView.adapter = filterAdapter
    }


}

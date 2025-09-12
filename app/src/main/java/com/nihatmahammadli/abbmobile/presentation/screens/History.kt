package com.nihatmahammadli.abbmobile.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentHistoryBinding
import com.nihatmahammadli.abbmobile.presentation.adapters.HistoryFilterAdapter
import com.nihatmahammadli.abbmobile.presentation.adapters.HistoryPaymentsAdapter
import com.nihatmahammadli.abbmobile.presentation.model.PaymentSummary
import com.nihatmahammadli.abbmobile.presentation.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class History : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by activityViewModels()
    private lateinit var filterAdapter: HistoryFilterAdapter
    private lateinit var historyAdapter: HistoryPaymentsAdapter

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        if (!viewModel.hasRefreshed && viewModel.paymentSum.value.isNullOrEmpty()) {
            viewModel.fetchTotalPayments()
            viewModel.hasRefreshed = true
        }
    }

    private fun initUI() {
        setObservers()
        setupSwipeRefresh()
        setUpAdapters()
        setupRecyclerView()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchTotalPayments()
        }
    }

    private fun setupRecyclerView() {
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.swipeRefreshLayout.isRefreshing = loading
        }

        viewModel.totalAmount.observe(viewLifecycleOwner) { total ->
            binding.amount.text = "%.2f ₼".format(total)
            val progressValue = if (total > 400) 400 else total.toInt()
            binding.progressBar2.progress = progressValue
        }

        viewModel.paymentSum.observe(viewLifecycleOwner) { list ->
            handlePaymentList(list)
        }
    }

    private fun handlePaymentList(list: List<PaymentSummary>?) {
        if (list.isNullOrEmpty()) {
            showEmptyState()
        } else {
            val sortedList = sortPaymentsByDate(list)
            showPaymentHistory(sortedList)
        }
    }

    private fun sortPaymentsByDate(list: List<PaymentSummary>): List<PaymentSummary> {
        return try {
            list.sortedWith { payment1, payment2 ->
                val date1 = dateFormatter.parse(payment1.date)
                val date2 = dateFormatter.parse(payment2.date)
                when {
                    date1 == null && date2 == null -> 0
                    date1 == null -> 1
                    date2 == null -> -1
                    else -> date2.compareTo(date1)
                }
            }
        } catch (e: Exception) {
            list.sortedByDescending { it.date }
        }
    }

    private fun showEmptyState() {
        binding.historyRecyclerView.visibility = View.GONE
        binding.emptyIcon.visibility = View.VISIBLE
        binding.emptyText.visibility = View.VISIBLE

    }

    private fun showPaymentHistory(sortedList: List<PaymentSummary>) {
        historyAdapter = HistoryPaymentsAdapter(sortedList)
        binding.historyRecyclerView.adapter = historyAdapter

        binding.historyRecyclerView.visibility = View.VISIBLE
        binding.emptyIcon.visibility = View.GONE
        binding.emptyText.visibility = View.GONE
    }

    private fun setUpAdapters() {
        val buttonList = listOf(
            getString(R.string.filter_direction),
            getString(R.string.filter_period),
            getString(R.string.filter_type),
            getString(R.string.filter_cards),
            getString(R.string.filter_calculation)
        )

        filterAdapter = HistoryFilterAdapter(buttonList)
        binding.rcyView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rcyView.adapter = filterAdapter
    }
}
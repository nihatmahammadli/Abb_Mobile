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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.presentation.adapters.HorizontalImageAdapter
import com.nihatmahammadli.abbmobile.presentation.adapters.TransactionAdapter
import com.nihatmahammadli.abbmobile.presentation.dashboard.home.model_home.Transaction

class HomePage : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var adapter: HorizontalImageAdapter
    private lateinit var trAdapter: TransactionAdapter
    private var isExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater,container,false)

        val transactions = getDummyTransactions()

        binding.recyclerViewTransactions.layoutManager = LinearLayoutManager(requireContext())
        trAdapter = TransactionAdapter(getDummyTransactions())
        binding.recyclerViewTransactions.adapter = trAdapter
        updateNoTransactionVisibility(transactions)



        binding.lastestTransaction.setOnClickListener {
            if (isExpanded) {
                slideUp(binding.recyclerViewTransactions)
            } else {
                slideDown(binding.recyclerViewTransactions)
            }
            isExpanded = !isExpanded
        }

        setupAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun setupAdapter(){
        val imageList = listOf(
            R.drawable.read_more_1,
            R.drawable.read_more_2,
            R.drawable.read_more_3,
        )
        adapter = HorizontalImageAdapter(imageList)
        binding.horizontalRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.horizontalRecyclerView.adapter = adapter
    }
    private fun getDummyTransactions(): List<Transaction> {
        return listOf(
            Transaction("Groceries", 75.5, "10.07.2025"),
            Transaction("Online Shopping", 150.0, "09.07.2025"),
            Transaction("Electricity Bill", 45.75, "08.07.2025"),
            Transaction("Taxi", 12.0, "07.07.2025"),
            Transaction("Restaurant", 30.0, "06.07.2025")
        )
    }

    private fun slideUp(view: View) {
        view.animate()
            .translationY(-view.height.toFloat())
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                view.visibility = View.GONE
                view.translationY = 0f
                view.alpha = 1f
            }
            .start()
    }

    private fun slideDown(view: View) {

        view.visibility = View.VISIBLE
        view.alpha = 0f
        view.post {
            view.translationY = -view.height.toFloat()
            view.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(300)
                .start()
        }
    }
    fun updateNoTransactionVisibility(transactions: List<Transaction>) {
        if (transactions.isEmpty()) {
            binding.noTransactionYet.visibility = View.VISIBLE
            binding.recyclerViewTransactions.visibility = View.GONE
        } else {
            binding.noTransactionYet.visibility = View.GONE
            binding.recyclerViewTransactions.visibility = View.VISIBLE
        }
    }
}

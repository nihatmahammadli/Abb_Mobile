package com.nihatmahammadli.abbmobile.presentation.dashboard.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.nihatmahammadli.abbmobile.databinding.FragmentHomePageBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.presentation.adapters.*
import com.nihatmahammadli.abbmobile.presentation.dashboard.home.model_home.Transaction
import com.nihatmahammadli.abbmobile.presentation.providers.CardProvider
import kotlin.math.abs

class HomePage : Fragment() {

    private lateinit var binding: FragmentHomePageBinding
    private lateinit var imageAdapter: HorizontalImageAdapter
    private lateinit var transactionAdapter: TransactionAdapter
    private var isExpanded = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)

        setupHorizontalAdapter()
        setupTransactionSection()
        changeViewPager()
        viewPagerOverlapEffect()
        setupQrButton()

        return binding.root
    }

    private fun setupHorizontalAdapter() {
        val imageList = listOf(
            R.drawable.read_more_1,
            R.drawable.read_more_2,
            R.drawable.read_more_3,
        )
        imageAdapter = HorizontalImageAdapter(imageList)
        binding.horizontalRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = imageAdapter
        }
    }

    private fun setupTransactionSection() {
        val transactions = getDummyTransactions()
        transactionAdapter = TransactionAdapter(transactions)
        binding.recyclerViewTransactions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
        }
        updateTransactionVisibility(transactions)

        binding.lastestTransaction.setOnClickListener {
            toggleTransactionList()
        }
    }

    private fun toggleTransactionList() {
        isExpanded = !isExpanded
        updateArrowIcon()

        val transactions = transactionAdapter.items

        if (isExpanded) {
            expandView(binding.recyclerViewTransactions)
        } else {
            collapseView(binding.recyclerViewTransactions)
        }
        updateTransactionVisibility(transactions)
    }

    private fun updateArrowIcon() {
        val icon = if (isExpanded) R.drawable.arrow_up else R.drawable.arrow_under
        binding.lastestTransaction.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0)
    }

    private fun updateTransactionVisibility(transactions: List<Transaction>) {
        val hasTransactions = transactions.isNotEmpty()
        if (isExpanded) {
            binding.noTransactionYet.visibility = if (hasTransactions) View.GONE else View.VISIBLE
            binding.recyclerViewTransactions.visibility = if (hasTransactions) View.VISIBLE else View.GONE
        } else {
            binding.recyclerViewTransactions.visibility = View.GONE
            binding.noTransactionYet.visibility = View.GONE
        }
    }

    private fun expandView(view: View) {
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

    private fun collapseView(view: View) {
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

    private fun getDummyTransactions(): List<Transaction> {
        return listOf(
            Transaction("Groceries", 75.5, "10.07.2025"),
            Transaction("Online Shopping", 150.0, "09.07.2025"),
            Transaction("Electricity Bill", 45.75, "08.07.2025"),
            Transaction("Taxi", 12.0, "07.07.2025"),
            Transaction("Restaurant", 30.0, "06.07.2025")
        )
    }

    fun changeViewPager() {
        val cards = CardProvider.getCards()

        val adapter = CardAdapter(cards) { position ->
            CardProvider.handleCardClick(position)
        }
        binding.viewPager.adapter = adapter
    }

    fun viewPagerOverlapEffect() {
        binding.viewPager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            setPageTransformer { page, position ->
                val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)

                when {
                    position < -1 -> {
                        page.alpha = 0f
                    }
                    position <= 1 -> {
                        val startOffset = -position * offsetPx
                        page.translationX = startOffset

                        val scaleFactor = 1 - 0.1f * abs(position)
                        page.scaleX = scaleFactor
                        page.scaleY = scaleFactor

                        page.alpha = 1 - 0.3f * abs(position)
                    }
                    else -> {
                        page.alpha = 0f
                    }
                }
            }
        }
    }


    private fun setupQrButton() {
        binding.qrButton.setOnClickListener {
            val options = ScanOptions()
            options.setPrompt("Scan any QR code")
            options.setBeepEnabled(true)
            options.setOrientationLocked(true)
            options.setCaptureActivity(CaptureActivity::class.java)
            barcodeLauncher.launch(options)
        }
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()){result ->
        if(result.contents != null){
            Toast.makeText(requireContext(), result.contents, Toast.LENGTH_SHORT).show()
        }else {
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}

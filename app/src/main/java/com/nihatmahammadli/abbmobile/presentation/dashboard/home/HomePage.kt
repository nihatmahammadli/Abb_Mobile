package com.nihatmahammadli.abbmobile.presentation.dashboard.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nihatmahammadli.abbmobile.databinding.FragmentHomePageBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.presentation.adapters.CardAdapter
import com.nihatmahammadli.abbmobile.presentation.adapters.HorizontalImageAdapter
import com.nihatmahammadli.abbmobile.presentation.adapters.TransactionAdapter
import com.nihatmahammadli.abbmobile.presentation.dashboard.home.model_home.Transaction
import com.nihatmahammadli.abbmobile.presentation.providers.CardProvider

class HomePage : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    private lateinit var imageAdapter: HorizontalImageAdapter
    private lateinit var transactionAdapter: TransactionAdapter
    private var isExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        setupHorizontalAdapter()
        setupTransactionSection()
        changeViewPager()
        viewPagerOverlapEffect()
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

    fun changeViewPager(){
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
                val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
                val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)

                when {
                    position < -1 -> { // Sol kənarda gizli olsun
                        page.alpha = 0f
                    }
                    position <= 1 -> {
                        // kartları overlap və üst-üstə qoy, altındakılar qismən görünsün
                        val startOffset = -position * offsetPx
                        page.translationX = startOffset

                        // Kartların ölçüsünü azacıq dəyişmək (istəyə görə)
                        val scaleFactor = 1 - 0.1f * kotlin.math.abs(position)
                        page.scaleX = scaleFactor
                        page.scaleY = scaleFactor

                        // Alpha-nı da tənzimlə, sağa sürüşəndə azalsın
                        page.alpha = 1 - 0.3f * kotlin.math.abs(position)
                    }
                    else -> { // Sağ kənarda gizli olsun
                        page.alpha = 0f
                    }
                }
            }
        }
    }

}

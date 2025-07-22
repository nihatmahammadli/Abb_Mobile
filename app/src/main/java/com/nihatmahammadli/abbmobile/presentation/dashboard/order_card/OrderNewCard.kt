package com.nihatmahammadli.abbmobile.presentation.dashboard.order_card

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentOrderNewCardBinding
import com.nihatmahammadli.abbmobile.domain.model.UiCard
import com.nihatmahammadli.abbmobile.presentation.adapters.AllCardsAdapter
import com.nihatmahammadli.abbmobile.presentation.providers.OrderCardProvider
import com.nihatmahammadli.abbmobile.presentation.viewmodel.CardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class OrderNewCard : Fragment() {
    private lateinit var binding: FragmentOrderNewCardBinding
    private val viewModel: CardViewModel by activityViewModels()
    private lateinit var adapter: AllCardsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderNewCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        binding.orderCardBtn.setOnClickListener {
            setupObservers()
        }
        viewPagerOverlapEffect()



        viewModel.fetchSingleCardFromApi()
    }

    private fun setupAdapter() {
        val cards = OrderCardProvider.getCards()
        adapter = AllCardsAdapter(cards)
        binding.viewPager.adapter = adapter
    }

    private fun setupObservers() {

        viewModel.cards.observe(viewLifecycleOwner) { cards ->
            if (cards.isNotEmpty()) {
                showToast("${cards.size} kart mövcuddur")
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.cardFetchResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                findNavController().navigateUp()
            } else {
                showToast("Təəssüf, mövcud kart tapılmadı.")
            }
        }
    }

    private fun viewPagerOverlapEffect() {
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
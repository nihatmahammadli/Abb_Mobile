package com.nihatmahammadli.abbmobile.presentation.dashboard.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentHomePageBinding
import com.nihatmahammadli.abbmobile.presentation.adapters.*
import com.nihatmahammadli.abbmobile.presentation.components.CardOrderSheet
import com.nihatmahammadli.abbmobile.presentation.model.BaseCardData
import com.nihatmahammadli.abbmobile.presentation.model.Transaction
import com.nihatmahammadli.abbmobile.presentation.providers.CardProvider
import com.nihatmahammadli.abbmobile.presentation.viewmodel.CardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class HomePage : Fragment() {

    private lateinit var binding: FragmentHomePageBinding
    private lateinit var imageAdapter: HorizontalImageAdapter
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var cardAdapter: CardAdapter
    private var isExpanded = false

    private val viewModel: CardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupObservers()


        if (!viewModel.hasFetchedCards) {
            viewModel.fetchCardsWithBalances()
        }

        if (!viewModel.hasFetchedCards) {
            viewModel.fetchCardsFromFirebase()
        }

        if (!viewModel.hasFetchedUserName) {
            viewModel.fetchUserNameFromFirebase()
        }
    }


    private fun initUI() {
        setupHorizontalRecycler()
        setupTransactionSection()
        setupQrScan()
        setupCardSection()
        applyViewPagerOverlap()
        goProfile()
        setupSwipeToRefresh()
    }
    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        viewModel.hasFetchedUserName = false
        viewModel.hasFetchedCards = false

        viewModel.fetchCardsFromFirebase()
        viewModel.fetchUserNameFromFirebase()
        viewModel.fetchCardsWithBalances()
    }


    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        viewModel.uiCards.observe(viewLifecycleOwner) { uiCards ->
            Log.d("HomePage", "Data gəldi, kartların sayı: ${uiCards.size}")
            uiCards.forEachIndexed { index, card ->
                Log.d("HomePage", "Card #$index: Number=${card.cardNumber}, Expiry=${card.expiryDate}, CVV=${card.cvv}")
            }
            updateCardAdapter(uiCards)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.txtName.text = "Hello $name"
        }
    }


    private fun updateCardAdapter(uiCards: List<com.nihatmahammadli.abbmobile.domain.model.UiCard>) {
        val cards = mutableListOf<BaseCardData>()

        cards.addAll(uiCards.map { card ->
            BaseCardData.CustomCard(
                title = "Mastercard",
                balance = "${card.balance} ₼",
                cardCodeEnding = "•••• ${card.cardNumber.takeLast(4)}",
                expiryDate = card.expiryDate,
                backgroundResId = R.drawable.card_background,
                cardLogoResId = R.drawable.visa_card,
                showVisa = true,
                onTopUpClick = { handleTopUpClick(card) },
                onPayClick = { handlePayClick(card) },
                onTransferClick = { handleTransferClick(card) }
            )
        })

        cards.addAll(CardProvider.getCards())

        cardAdapter.updateItems(cards)
    }


    private fun handleTopUpClick(card: com.nihatmahammadli.abbmobile.domain.model.UiCard) {
        findNavController().navigate(R.id.action_homePage_to_topUp)
    }

    private fun handlePayClick(card: com.nihatmahammadli.abbmobile.domain.model.UiCard) {
        showToast("Ödəniş: ${card.cardNumber.takeLast(4)}")
    }

    private fun handleTransferClick(card: com.nihatmahammadli.abbmobile.domain.model.UiCard) {
        showToast("Köçürmə: ${card.cardNumber.takeLast(4)}")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setupHorizontalRecycler() {
        val imageList = listOf(
            R.drawable.read_more_1,
            R.drawable.read_more_2,
            R.drawable.read_more_3
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

        binding.recyclerViewTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTransactions.adapter = transactionAdapter

        binding.lastestTransaction.setOnClickListener { toggleTransactionList(transactions) }

        updateTransactionUI(transactions)
    }

    private fun toggleTransactionList(transactions: List<Transaction>) {
        isExpanded = !isExpanded
        updateArrowIcon()
        updateTransactionUI(transactions)
    }

    private fun updateArrowIcon() {
        val icon = if (isExpanded) R.drawable.arrow_up else R.drawable.arrow_under
        binding.lastestTransaction.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0)
    }

    private fun updateTransactionUI(transactions: List<Transaction>) {
        if (isExpanded) {
            if (transactions.isEmpty()) {
                binding.recyclerViewTransactions.visibility = View.GONE
                binding.noTransactionYet.visibility = View.VISIBLE
            } else {
                binding.recyclerViewTransactions.visibility = View.VISIBLE
                binding.noTransactionYet.visibility = View.GONE
            }
        } else {
            binding.recyclerViewTransactions.visibility = View.GONE
            binding.noTransactionYet.visibility = View.GONE
        }
    }

    private fun getDummyTransactions(): List<Transaction> {
        return listOf(
            Transaction("Market", 75.5, "10.07.2025"),
            Transaction("Online alış-veriş", 150.0, "09.07.2025"),
            Transaction("İşıq pulu", 45.75, "08.07.2025"),
            Transaction("Taksi", 12.0, "07.07.2025"),
            Transaction("Restoran", 30.0, "06.07.2025")
        )
    }

    private fun setupQrScan() {
        binding.qrButton.setOnClickListener {
            val options = ScanOptions().apply {
                setPrompt("QR kodu oxudun")
                setBeepEnabled(true)
                setOrientationLocked(true)
                setCaptureActivity(CaptureActivity::class.java)
            }
            barcodeLauncher.launch(options)
        }
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        val msg = result.contents ?: "Ləğv edildi"
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun setupCardSection() {
        cardAdapter = CardAdapter(mutableListOf()) { position ->
            if (position == 1 || position == 0) {
                showCardOrderBottomSheet()
            } else {
                val card = cardAdapter.getCardAt(position)
                if (card is BaseCardData.CustomCard) {
                    showToast("Kart seçildi: ${card.title}")
                }
            }
        }

        binding.viewPager.adapter = cardAdapter
    }

    private fun showCardOrderBottomSheet() {
        val sheet = CardOrderSheet()
        sheet.show(parentFragmentManager, CardOrderSheet.TAG)
    }

    private fun applyViewPagerOverlap() {
        binding.viewPager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            setPageTransformer { page, position ->
                val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)
                when {
                    position < -1 -> page.alpha = 0f
                    position <= 1 -> {
                        page.translationX = -position * offsetPx
                        val scale = 1 - 0.1f * abs(position)
                        page.scaleX = scale
                        page.scaleY = scale
                        page.alpha = 1 - 0.3f * abs(position)
                    }
                    else -> page.alpha = 0f
                }
            }
        }
    }

    fun goProfile() {
        val clickListener = View.OnClickListener {
            findNavController().navigate(R.id.action_homePage_to_profile)
        }

        binding.profileButton.setOnClickListener(clickListener)
        binding.txtName.setOnClickListener(clickListener)
        binding.textView13.setOnClickListener(clickListener)
    }

}
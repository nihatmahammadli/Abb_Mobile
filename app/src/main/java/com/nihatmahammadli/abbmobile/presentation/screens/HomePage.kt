package com.nihatmahammadli.abbmobile.presentation.screens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.nihatmahammadli.abbmobile.MainActivity
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentHomePageBinding
import com.nihatmahammadli.abbmobile.domain.model.UiCard
import com.nihatmahammadli.abbmobile.presentation.adapters.CardAdapter
import com.nihatmahammadli.abbmobile.presentation.adapters.HorizontalImageAdapter
import com.nihatmahammadli.abbmobile.presentation.adapters.TransactionAdapter
import com.nihatmahammadli.abbmobile.presentation.components.providers.CardProvider
import com.nihatmahammadli.abbmobile.presentation.components.sheet.AiChatDialogFragment
import com.nihatmahammadli.abbmobile.presentation.components.sheet.CardOrderSheet
import com.nihatmahammadli.abbmobile.presentation.model.BaseCardData
import com.nihatmahammadli.abbmobile.presentation.model.PaymentSummary
import com.nihatmahammadli.abbmobile.presentation.viewmodel.CardViewModel
import com.nihatmahammadli.abbmobile.presentation.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

@AndroidEntryPoint
class HomePage : Fragment() {

    private lateinit var binding: FragmentHomePageBinding
    private lateinit var imageAdapter: HorizontalImageAdapter
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var lastUiCards: List<UiCard>
    private lateinit var cardAdapter: CardAdapter
    private var isExpanded = false
    private var isSeen = false

    private val viewModel: CardViewModel by activityViewModels()
    private val historyViewModel: HistoryViewModel by activityViewModels()

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
            viewModel.fetchUserNameFromFirebase()
            viewModel.fetchCardsFromFirebase()
        }
        viewModel.listenToCardTransactions()
        viewModel.fetchTotalCashback()
    }

//    fun testNotification(){
//        binding.createAutoPayment.setOnClickListener {
//            NotificationHelper.generateNotification(
//                requireContext(),
//                "Welcome to ABB Mobile",
//                "You have successfully signed up",
//            )
//        }
//    }


    private fun initUI() {
        setupHorizontalRecycler()
        getHistory()
        goHistory()
        goProfile()
        setupQrScan()
        setupCardSection()
        applyViewPagerOverlap()
        goProfile()
        setupSwipeToRefresh()
        changeEyeIcon()
        supportAI()
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    fun supportAI() {
        binding.fabSupport.setOnClickListener {
            val dialog = AiChatDialogFragment()
            dialog.show(childFragmentManager, "AIChat")
        }
    }

    private fun refreshData() {
        viewModel.hasFetchedUserName = false
        viewModel.hasFetchedCards = false

        viewModel.fetchCardsFromFirebase()
        viewModel.fetchUserNameFromFirebase()
        viewModel.fetchCardsWithBalances()
        viewModel.fetchTotalCashback()
        historyViewModel.fetchTotalPayments()
    }


    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun setupObservers() {
        viewModel.uiCards.observe(viewLifecycleOwner) { uiCards ->
            Log.d("HomePage", "Data gəldi, kartların sayı: ${uiCards.size}")
            uiCards.forEachIndexed { index, card ->
                Log.d(
                    "HomePage",
                    "Card #$index: Number=${card.cardNumber}, Expiry=${card.expiryDate}, CVV=${card.cvv}"
                )
            }
            lastUiCards = uiCards
            updateCardAdapter(uiCards)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.txtName.text = getString(R.string.hello_user, name)
        }

        viewModel.cashbackTotal.observe(viewLifecycleOwner) { cashback ->
            if (isSeen) {
                binding.cashBackBtn.text = "${String.format("%.2f", cashback)} \nCashback₼"
            } else {
                binding.cashBackBtn.text = "••••"
            }
        }

        historyViewModel.paymentSum.observe(viewLifecycleOwner) { list ->
            val sortedList = list.sortedByDescending {
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(it.date)
            }
            val transactions = sortedList.take(10)

            transactionAdapter = TransactionAdapter(transactions)


            binding.recyclerViewTransactions.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewTransactions.adapter = transactionAdapter

            binding.lastestTransaction.setOnClickListener { toggleTransactionList(transactions) }


            updateTransactionUI(transactions)

        }


    }

    @SuppressLint("DefaultLocale")
    private fun updateCardAdapter(uiCards: List<UiCard>) {
        val cards = mutableListOf<BaseCardData>()

        if (isSeen) {
            cards.addAll(uiCards.map { card ->
                BaseCardData.CustomCard(
                    title = "Mastercard",
                    balance = "${"%.2f".format(card.balance)} ₼",
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
        } else {
            cards.addAll(uiCards.map { card ->
                BaseCardData.CustomCard(
                    title = "Mastercard",
                    balance = "•••• ₼",
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
        }

        cards.addAll(CardProvider.getCards())
        cardAdapter.updateItems(cards)
    }

    private fun handleTopUpClick(card: UiCard) {
        findNavController().navigate(R.id.action_homePage_to_topUp)
    }

    private fun handlePayClick(card: UiCard) {
        findNavController().navigate(R.id.action_homePage_to_payments)
    }

    private fun handleTransferClick(card: UiCard) {
        findNavController().navigate(R.id.action_homePage_to_transfer)
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
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = imageAdapter
        }
    }


    private fun toggleTransactionList(transactions: List<PaymentSummary>) {
        isExpanded = !isExpanded
        updateArrowIcon()
        updateTransactionUI(transactions)
    }

    private fun updateArrowIcon() {
        val icon = if (isExpanded) R.drawable.arrow_up else R.drawable.arrow_under
        binding.lastestTransaction.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0)
    }

    private fun updateTransactionUI(transactions: List<PaymentSummary>) {
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

    private fun getHistory() {
        historyViewModel.fetchTotalPayments()
    }

    private fun goHistory() {
        binding.constraintLayoutTransaction.setOnClickListener {
            findNavController().navigate(
                R.id.action_homePage_to_history,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.history, true)
                    .build()
            )
            (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                .selectedItemId = R.id.history

        }
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
        cardAdapter = CardAdapter(
            mutableListOf(),
            onCardButtonClick = { position ->
                if (position == 1 || position == 0) {
                    showCardOrderBottomSheet()
                } else {
                    val card = cardAdapter.getCardAt(position)
                    if (card is BaseCardData.CustomCard) {
                        showToast("Button clicked on kart: ${card.title}")
                    }
                }
            },
            onCustomCardClick = { position ->
                val card = cardAdapter.getCardAt(position)
                if (card is BaseCardData.CustomCard) {
                    findNavController().navigate(R.id.action_homePage_to_cardView)
                }
            }
        )

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

    @SuppressLint("SetTextI18n", "DefaultLocale")
    fun changeEyeIcon() {
        val sharedPref = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        isSeen = sharedPref.getBoolean("isSeen", false)

        binding.eyeIcon.setImageResource(
            if (isSeen) R.drawable.eye_open else R.drawable.eye_closed
        )

        if (!isSeen) {
            binding.cashBackBtn.text = "••••"
            binding.qrButton.text = "••••"
        } else {
            val cashbackValue = viewModel.cashbackTotal.value ?: 0.0
            binding.cashBackBtn.text = "${String.format("%.2f", cashbackValue)} \nCashback₼"
            binding.qrButton.text = getString(R.string._0_npending_edv)
        }

        viewModel.cashbackTotal.observe(viewLifecycleOwner) { cashback ->
            if (isSeen) {
                binding.cashBackBtn.text = "${"%.2f".format(cashback)} \nCashback₼"
                binding.qrButton.text = getString(R.string._0_npending_edv)
            } else {
                binding.cashBackBtn.text = "••••"
                binding.qrButton.text = "••••"
            }
        }

        binding.eyeIcon.setOnClickListener {
            isSeen = !isSeen
            val iconRes = if (isSeen) R.drawable.eye_open else R.drawable.eye_closed
            binding.eyeIcon.setImageResource(iconRes)

            sharedPref.edit { putBoolean("isSeen", isSeen) }

            updateCardAdapter(lastUiCards)

            if (!isSeen) {
                binding.cashBackBtn.text = "••••"
                binding.qrButton.text = "••••"
            } else {
                val cashbackValue = viewModel.cashbackTotal.value ?: 0.0
                binding.cashBackBtn.text = "${"%.2f".format(cashbackValue)} \nCashback₼"
                binding.qrButton.text = getString(R.string._0_npending_edv)
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
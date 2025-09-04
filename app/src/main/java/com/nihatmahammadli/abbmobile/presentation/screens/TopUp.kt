package com.nihatmahammadli.abbmobile.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentTopUpBinding
import com.nihatmahammadli.abbmobile.presentation.components.sheet.CardOrderSheet
import com.nihatmahammadli.abbmobile.presentation.components.sheet.ItemMyCardSheet
import com.nihatmahammadli.abbmobile.presentation.components.sheet.SelectTopUpSheet
import com.nihatmahammadli.abbmobile.presentation.viewmodel.CardViewModel
import com.nihatmahammadli.abbmobile.presentation.viewmodel.TopUpViewModel
import com.nihatmahammadli.abbmobile.presentation.viewmodel.TopUpResult
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

@AndroidEntryPoint
class TopUp : Fragment() {
    private lateinit var binding: FragmentTopUpBinding
    private var lastSelectedOption: String? = null
    private val topUpViewModel: TopUpViewModel by viewModels()
    private var totalAmount = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopUpBinding.inflate(inflater, container, false)


        initUI()
        return binding.root
    }

    fun initUI(){
        setupClickListeners()
        setupAmountButtons()
        observeViewModel()
        disableBottomSection()
        fetchAmountFromFirebase()
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
        fun fetchAmountFromFirebase(){
            topUpViewModel.listenToAmount()

            topUpViewModel.totalBalance.observe(viewLifecycleOwner){total ->
                binding.masterCardBalance.text = String.format("%,.2f AZN", total)
            }
        }

    private fun setupClickListeners() {
        binding.transferBtn.setOnClickListener {
            sendTopUpDataToFirebase()
        }

        binding.leftBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeViewModel() {
        topUpViewModel.senderSelected.observe(viewLifecycleOwner) { isSelected ->
            if (isSelected) {
                enableBottomSection()
            } else {
                disableBottomSection()
            }
        }

        topUpViewModel.topUpResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is TopUpResult.Success -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    resetAmount()
                    topUpViewModel.clearResult()
                }
                is TopUpResult.Error -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    topUpViewModel.clearResult()
                }
                null -> {
                }
            }
        }

        topUpViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.transferBtn.isEnabled = !isLoading && (topUpViewModel.senderSelected.value == true)
            binding.transferBtn.text = if (isLoading) "Əlavə edilir..." else "Əlavə et"

            setAmountControlsEnabled(!isLoading)
        }
    }

    private fun showTopUpSheet() {
        binding.senderBtn.setOnClickListener {
            SelectTopUpSheet(
                selectedOption = lastSelectedOption,
                onCardSelected = { selectedCard ->
                    lastSelectedOption = selectedCard

                    val iconRes = if (selectedCard == "ApplyPay") R.drawable.apple_pay_ic
                    else R.drawable.credit_cart_icon

                    val newIcon = ContextCompat.getDrawable(requireContext(), iconRes)
                    binding.senderBtn.setCompoundDrawablesWithIntrinsicBounds(newIcon, null, null, null)

                    topUpViewModel.setSenderSelected(true)
                }
            ).show(parentFragmentManager, "SelectTopUpSheet")
        }
    }

    private fun showMyCardSheet() {
        binding.masterCard.setOnClickListener {
            val sheet = ItemMyCardSheet()
            sheet.show(parentFragmentManager, CardOrderSheet.TAG)
            val newIcon = ContextCompat.getDrawable(requireContext(), R.drawable.credit_cart_icon)
            binding.senderBtn.setCompoundDrawablesWithIntrinsicBounds(newIcon, null, null, null)
            topUpViewModel.setSenderSelected(true)
        }
    }

    private fun setAmountControlsEnabled(enabled: Boolean) {
        binding.plus1.isEnabled = enabled
        binding.plus2.isEnabled = enabled
        binding.plus3.isEnabled = enabled
        binding.plus4.isEnabled = enabled
        binding.topUpAmount.isEnabled = enabled

        val alpha = if (enabled) 1f else 0.5f
        binding.plus1.alpha = alpha
        binding.plus2.alpha = alpha
        binding.plus3.alpha = alpha
        binding.plus4.alpha = alpha
        binding.topUpAmount.alpha = alpha
    }

    private fun disableBottomSection() {
        setAmountControlsEnabled(false)
        binding.transferBtn.isEnabled = false
        binding.aznText.isEnabled = false

        val alpha = 0.5f
        binding.transferBtn.alpha = alpha
        binding.aznText.alpha = alpha
    }

    private fun enableBottomSection() {
        setAmountControlsEnabled(true)
        binding.transferBtn.isEnabled = true
        binding.aznText.isEnabled = true

        val alpha = 1f
        binding.transferBtn.alpha = alpha
        binding.aznText.alpha = alpha
    }

    @SuppressLint("SetTextI18n")
    private fun setupAmountButtons() {
        binding.plus1.setOnClickListener { addAmount(1.0) }
        binding.plus2.setOnClickListener { addAmount(10.0) }
        binding.plus3.setOnClickListener { addAmount(50.0) }
        binding.plus4.setOnClickListener { setMaxAmount() }

        binding.topUpAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString().replace(',', '.')
                val parsedAmount = text.toDoubleOrNull() ?: 0.0
                totalAmount = (parsedAmount * 100.0).roundToInt() / 100.0
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        showTopUpSheet()
        showMyCardSheet()
    }

    private fun addAmount(amount: Double) {
        totalAmount += amount
        totalAmount = (totalAmount * 100.0).roundToInt() / 100.0
        updateAmountField()
    }

    private fun setMaxAmount() {
        totalAmount = 4978.0
        updateAmountField()
    }

    private fun updateAmountField() {
        val formattedAmount = BigDecimal(totalAmount)
            .setScale(2, RoundingMode.HALF_UP)
            .toString()
        binding.topUpAmount.setText(formattedAmount)
        binding.topUpAmount.setSelection(binding.topUpAmount.text.length)
    }

    private fun resetAmount() {
        totalAmount = 0.0
        binding.topUpAmount.setText("0.00")
    }

    private fun sendTopUpDataToFirebase() {
        val sender = lastSelectedOption ?: run {
            Toast.makeText(requireContext(), "Zəhmət olmasa kart seçin", Toast.LENGTH_SHORT).show()
            return
        }

        val amountText = binding.topUpAmount.text.toString().replace(',', '.')
        val amount = amountText.toDoubleOrNull() ?: run {
            Toast.makeText(requireContext(), "Məbləğ düzgün deyil", Toast.LENGTH_SHORT).show()
            return
        }

        if (amount <= 0.0) {
            Toast.makeText(requireContext(), "Məbləğ sıfırdan böyük olmalıdır", Toast.LENGTH_SHORT).show()
            return
        }

        val roundedAmount = (amount * 100.0).roundToInt() / 100.0
        topUpViewModel.saveAmountInFirebase(roundedAmount, sender)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        topUpViewModel.clearResult()
    }
}
package com.nihatmahammadli.abbmobile.presentation.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nihatmahammadli.abbmobile.databinding.FragmentPaymentsAmountBinding
import com.nihatmahammadli.abbmobile.presentation.viewmodel.PaymentAmountsViewModel
import com.nihatmahammadli.abbmobile.presentation.viewmodel.TransferResult
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

@AndroidEntryPoint
class PaymentsAmount : Fragment() {
    private lateinit var binding: FragmentPaymentsAmountBinding
    private val viewModel: PaymentAmountsViewModel by viewModels()
    val args: PaymentsAmountArgs by navArgs()

    private var totalAmount = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentsAmountBinding.inflate(inflater, container, false)

        val cardNumber = args.cardNumber


        if (!cardNumber.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Kart nömrəsi: $cardNumber", Toast.LENGTH_SHORT).show()
        }



        setupAmountButtons()
        setupTransferButton()
        observeViewModel()
        goBack()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearResult()
    }

    private fun setupTransferButton() {
        val fromTransferPage = args.fromTransferPage


        binding.transferBtn.setOnClickListener {
            val amountText = binding.topUpAmount.text.toString().replace(',', '.')
            val amount = amountText.toDoubleOrNull()


            when {
                amount == null -> Toast.makeText(
                    requireContext(), "Məbləğ düzgün deyil", Toast.LENGTH_SHORT
                ).show()

                amount <= 0.0 -> Toast.makeText(
                    requireContext(), "Məbləğ sıfırdan böyük olmalıdır", Toast.LENGTH_SHORT
                ).show()

                else -> {
                    viewModel.transferAmount(
                        amount = amount,
                        paymentType = "payment",
                        applyCashBack = !args.fromTransferPage,
                        paymentFor = paymentForValue
                    )
                }
            }
        }

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
    }

    private val paymentForValue: String? by lazy {
        when {
            args.paymentVertical != null -> args.paymentVertical?.title
            args.paymentHorizontal != null -> args.paymentHorizontal?.title
            !args.cardNumber.isNullOrEmpty() -> args.cardNumber
            else -> null
        }
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
        val formattedAmount = BigDecimal(totalAmount).setScale(2, RoundingMode.HALF_UP).toString()
        binding.topUpAmount.setText(formattedAmount)
        binding.topUpAmount.setSelection(binding.topUpAmount.text.length)
    }

    @SuppressLint("SetTextI18n")
    private fun resetAmount() {
        totalAmount = 0.0
        binding.topUpAmount.setText("0.00")
    }

    private fun observeViewModel() {
        viewModel.transferResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is TransferResult.Success -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    resetAmount()
                    viewModel.clearResult()
                }

                is TransferResult.Error -> {
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    viewModel.clearResult()
                }

                null -> {
                }
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.transferBtn.isEnabled = !isLoading
            binding.transferBtn.text = if (isLoading) "Sending..." else "Transfer et"

            binding.plus1.isEnabled = !isLoading
            binding.plus2.isEnabled = !isLoading
            binding.plus3.isEnabled = !isLoading
            binding.plus4.isEnabled = !isLoading
            binding.topUpAmount.isEnabled = !isLoading

        }
    }


    fun goBack() {
        binding.leftBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
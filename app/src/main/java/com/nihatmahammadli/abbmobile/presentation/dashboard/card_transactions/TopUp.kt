package com.nihatmahammadli.abbmobile.presentation.dashboard.card_transactions

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.FragmentTopUpBinding
import com.nihatmahammadli.abbmobile.presentation.components.CardOrderSheet
import com.nihatmahammadli.abbmobile.presentation.components.ItemMyCardSheet
import com.nihatmahammadli.abbmobile.presentation.components.SelectTopUpSheet
import com.nihatmahammadli.abbmobile.presentation.viewmodel.TopUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopUp : Fragment() {
    private lateinit var binding: FragmentTopUpBinding
    private var lastSelectedOption: String? = null
    private val topUpViewModel: TopUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopUpBinding.inflate(inflater,container,false)
        binding.transferBtn.setOnClickListener {
            sendTopUpDataToFirebase()
        }

        goBack()
        showTopUpSheet()
        disableBottomSection()
        observeViewModel()
        changeAmount()
        showMyCardSheet()
        return binding.root
    }

    private fun observeViewModel() {
        topUpViewModel.senderSelected.observe(viewLifecycleOwner) { isSelected ->
            if (isSelected) {
                enableBottomSection()
            } else {
                disableBottomSection()
            }
        }

        topUpViewModel.topUpResult.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(requireContext(), "Uğurla əlavə olundu!", Toast.LENGTH_SHORT).show()
                totalAmount = 0
                binding.topUpAmount.setText("0")
            } else {
                Toast.makeText(requireContext(), "Xəta baş verdi", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun goBack(){
        binding.leftBtn.setOnClickListener {
            findNavController().navigateUp()
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

    private fun showMyCardSheet(){
        binding.masterCard.setOnClickListener {
            val sheet = ItemMyCardSheet()
            sheet.show(parentFragmentManager, CardOrderSheet.TAG)
            val newIcon = ContextCompat.getDrawable(requireContext(), R.drawable.credit_cart_icon)
            binding.senderBtn.setCompoundDrawablesWithIntrinsicBounds(newIcon, null, null, null)

        }
    }

    private fun disableBottomSection() {
        binding.plus1.isEnabled = false
        binding.plus2.isEnabled = false
        binding.plus3.isEnabled = false
        binding.plus4.isEnabled = false
        binding.transferBtn.isEnabled = false
        binding.topUpAmount.isEnabled = false
        binding.aznText.isEnabled = false

        val alpha = 0.5f
        binding.plus1.alpha = alpha
        binding.plus2.alpha = alpha
        binding.plus3.alpha = alpha
        binding.aznText.alpha = alpha
        binding.plus4.alpha = alpha
        binding.transferBtn.alpha = alpha
        binding.topUpAmount.alpha = alpha
    }

    private fun enableBottomSection() {
        binding.plus1.isEnabled = true
        binding.plus2.isEnabled = true
        binding.plus3.isEnabled = true
        binding.plus4.isEnabled = true
        binding.transferBtn.isEnabled = true
        binding.topUpAmount.isEnabled = true
        binding.aznText.isEnabled = true

        val alpha = 1f
        binding.plus1.alpha = alpha
        binding.aznText.alpha = alpha
        binding.plus2.alpha = alpha
        binding.plus3.alpha = alpha
        binding.plus4.alpha = alpha
        binding.transferBtn.alpha = alpha
        binding.topUpAmount.alpha = alpha
    }

    var totalAmount = 0

    @SuppressLint("SetTextI18n")
    fun changeAmount() {
        val plus1 = binding.plus1
        val plus2 = binding.plus2
        val plus3 = binding.plus3
        val max = binding.plus4

        plus1.setOnClickListener {
            totalAmount += 1
            binding.topUpAmount.setText("$totalAmount")
        }
        plus2.setOnClickListener {
            totalAmount += 10
            binding.topUpAmount.setText("$totalAmount")
        }
        plus3.setOnClickListener {
            totalAmount += 50
            binding.topUpAmount.setText("$totalAmount")
        }
        max.setOnClickListener {
            binding.topUpAmount.setText("4.978.34 ₼")
            totalAmount = 0
        }
    }

    private fun sendTopUpDataToFirebase() {
        val sender = lastSelectedOption ?: run {
            Toast.makeText(requireContext(), "Zəhmət olmasa kart seçin", Toast.LENGTH_SHORT).show()
            return
        }

        val amountText = binding.topUpAmount.text.toString()
        val amount = amountText.toIntOrNull() ?: run {
            Toast.makeText(requireContext(), "Məbləğ düzgün deyil", Toast.LENGTH_SHORT).show()
            return
        }

        topUpViewModel.saveAmountInFirebase(amount, sender)
    }

}
package com.nihatmahammadli.abbmobile.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.databinding.ItemHistoryPaymentsBinding
import com.nihatmahammadli.abbmobile.presentation.model.PaymentSummary

class HistoryPaymentsAdapter(
    private val list: List<PaymentSummary>
) : RecyclerView.Adapter<HistoryPaymentsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemHistoryPaymentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = list[position]
        holder.binding.titleTextView.text = item.paymentFor
        holder.binding.amountTextView.text = "%.2f ₼".format(item.totalAmount)
        holder.binding.dateTextView.text = item.date
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: ItemHistoryPaymentsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
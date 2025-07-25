package com.nihatmahammadli.abbmobile.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.databinding.ItemTransactionBinding
import com.nihatmahammadli.abbmobile.presentation.model.PaymentSummary
import com.nihatmahammadli.abbmobile.presentation.model.Transaction

class TransactionAdapter(val items: List<PaymentSummary>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = items[position]
        holder.binding.titleTextView.text = item.paymentFor
        holder.binding.amountTextView.text = "%.2f ₼".format(item.totalAmount)
        holder.binding.dateTextView.text = item.date
    }

    override fun getItemCount(): Int = items.size

    inner class TransactionViewHolder(val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root)
}

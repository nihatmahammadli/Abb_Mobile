package com.nihatmahammadli.abbmobile.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.databinding.ItemPaymentVerticalBinding
import com.nihatmahammadli.abbmobile.presentation.model.PaymentVertical

class PaymentVerticalAdapter(
    val list: List<PaymentVertical>,
    val onItemClicked: (PaymentVertical) -> Unit
): RecyclerView.Adapter<PaymentVerticalAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentVerticalAdapter.ViewHolder {
        val binding = ItemPaymentVerticalBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: ItemPaymentVerticalBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: PaymentVertical) = with(binding){
            image.setImageResource(item.imageRes)
            title.text = item.title
        }
    }
}
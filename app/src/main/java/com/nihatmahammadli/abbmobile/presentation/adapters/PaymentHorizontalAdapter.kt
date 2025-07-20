package com.nihatmahammadli.abbmobile.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.databinding.ItemPaymentsHorizontalBinding
import com.nihatmahammadli.abbmobile.presentation.model.PaymentHorizontal

class PaymentHorizontalAdapter(
    val imageList: List<PaymentHorizontal>,
    val onClick: (PaymentHorizontal) -> Unit
    ): RecyclerView.Adapter<PaymentHorizontalAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentHorizontalAdapter.ViewHolder {
        val binding = ItemPaymentsHorizontalBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = imageList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class ViewHolder(val binding: ItemPaymentsHorizontalBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: PaymentHorizontal) = with(binding){
            image.setImageResource(item.imageRes)
            title.text = item.title
        }
    }
}
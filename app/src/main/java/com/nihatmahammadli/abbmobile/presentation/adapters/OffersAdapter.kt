package com.nihatmahammadli.abbmobile.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.databinding.ItemOffersViewBinding

class OffersAdapter(val imageList: List<Int>) :
    RecyclerView.Adapter<OffersAdapter.OffersViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OffersAdapter.OffersViewHolder {
        val binding =
            ItemOffersViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OffersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OffersAdapter.OffersViewHolder, position: Int) {
        val image = imageList[position]
        holder.binding.img.setImageResource(image)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class OffersViewHolder(val binding: ItemOffersViewBinding) :
        RecyclerView.ViewHolder(binding.root)
}
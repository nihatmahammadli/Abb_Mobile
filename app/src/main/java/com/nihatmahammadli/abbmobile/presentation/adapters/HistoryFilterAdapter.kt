package com.nihatmahammadli.abbmobile.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.databinding.ItemHistoryFilterBinding

class HistoryFilterAdapter(
    private val buttonText: List<String>
) : RecyclerView.Adapter<HistoryFilterAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryFilterAdapter.ViewHolder {
        val binding =
            ItemHistoryFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryFilterAdapter.ViewHolder, position: Int) {
        val button = buttonText[position]
        holder.binding.text.text = button
    }

    override fun getItemCount(): Int {
        return buttonText.size
    }

    inner class ViewHolder(val binding: ItemHistoryFilterBinding) :
        RecyclerView.ViewHolder(binding.root)
}
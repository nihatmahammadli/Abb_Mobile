package com.nihatmahammadli.abbmobile.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.databinding.ItemSettingsBinding
import com.nihatmahammadli.abbmobile.presentation.model.SettingsItem

class SettingsAdapter(
    private val items: List<SettingsItem>,
    val onItemClick: (SettingsItem, Int) -> Unit   // position də göndərək
) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SettingsItem) {
            binding.itemIcon.setImageResource(item.icon)
            binding.itemTitle.text = item.title

            if (item.subtitle != null) {
                binding.itemSubtitle.text = item.subtitle
                binding.itemSubtitle.visibility = View.VISIBLE
            } else {
                binding.itemSubtitle.visibility = View.GONE
            }

            if (item.actionText != null) {
                binding.itemAction.text = item.actionText
                binding.itemAction.visibility = View.VISIBLE
            } else {
                binding.itemAction.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSettingsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        if (position == 2) {
            holder.itemView.setOnClickListener {
                onItemClick(item, position)
            }
        } else {
            holder.itemView.setOnClickListener(null)
        }
    }
}

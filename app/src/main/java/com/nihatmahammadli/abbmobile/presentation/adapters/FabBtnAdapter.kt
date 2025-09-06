package com.nihatmahammadli.abbmobile.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.databinding.ItemFabRecyclerBinding
import com.nihatmahammadli.abbmobile.presentation.model.FabBtnRecyclerItems

class FabBtnAdapter(
    private val list: List<FabBtnRecyclerItems>,
    private val onItemClick: (Int, FabBtnRecyclerItems) -> Unit
) : RecyclerView.Adapter<FabBtnAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFabRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(private val binding: ItemFabRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FabBtnRecyclerItems, position: Int) {
            binding.icon.setImageResource(item.icon)
            binding.title.text = item.title

            if (position < 3) {
                binding.root.setOnClickListener {
                    onItemClick(position, item)
                }
            } else {
                binding.root.setOnClickListener(null)
            }
        }
    }
}

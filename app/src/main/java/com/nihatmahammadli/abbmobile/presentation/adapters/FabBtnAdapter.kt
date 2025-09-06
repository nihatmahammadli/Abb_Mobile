package com.nihatmahammadli.abbmobile.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.databinding.ItemFabRecyclerBinding
import com.nihatmahammadli.abbmobile.presentation.model.FabBtnRecyclerItems

class FabBtnAdapter(val list: List<FabBtnRecyclerItems>): RecyclerView.Adapter<FabBtnAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemFabRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = list[position]
        holder.binding.icon.setImageResource(item.icon)
        holder.binding.title.text = item.title
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: ItemFabRecyclerBinding): RecyclerView.ViewHolder(binding.root)
}
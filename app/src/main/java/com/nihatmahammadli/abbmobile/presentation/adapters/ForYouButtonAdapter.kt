package com.nihatmahammadli.abbmobile.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.databinding.ItemForYouButtonBinding
import com.nihatmahammadli.abbmobile.presentation.model.ForYouButton

class ForYouButtonAdapter : RecyclerView.Adapter<ForYouButtonAdapter.ViewHolder>() {

    private var forYouButtons: List<ForYouButton> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<ForYouButton>) {
        forYouButtons = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemForYouButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = forYouButtons[position]
        holder.binding.iconView.setImageResource(item.icon)
        holder.binding.title.text = item.title
        holder.binding.amount.text = item.amount.toString()
    }

    override fun getItemCount(): Int = forYouButtons.size

    inner class ViewHolder(val binding: ItemForYouButtonBinding) :
        RecyclerView.ViewHolder(binding.root)
}

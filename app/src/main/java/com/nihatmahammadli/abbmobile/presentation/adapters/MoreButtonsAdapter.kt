package com.nihatmahammadli.abbmobile.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.R
import com.nihatmahammadli.abbmobile.databinding.ItemMoreButtonsBinding
import com.nihatmahammadli.abbmobile.presentation.model.MoreButton

class MoreButtonsAdapter(
    private val buttonList: List<MoreButton>,
    private val onButtonClick: (position: Int) -> Unit
) : RecyclerView.Adapter<MoreButtonsAdapter.ViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMoreButtonsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(buttonList[position], position)
    }

    override fun getItemCount(): Int = buttonList.size

    inner class ViewHolder(private val binding: ItemMoreButtonsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MoreButton, position: Int) {
            binding.btn.text = itemView.context.getString(item.titleResId)

            val context = itemView.context
            if (position == selectedPosition) {
                binding.btn.backgroundTintList = ContextCompat.getColorStateList(context, R.color.blue)
                binding.btn.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                binding.btn.backgroundTintList = ContextCompat.getColorStateList(context, R.color.white)
                binding.btn.setTextColor(ContextCompat.getColor(context, R.color.black))
            }

            binding.btn.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = position

                if (previousPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousPosition)
                }
                notifyItemChanged(selectedPosition)

                onButtonClick(position)
            }
        }

    }
}

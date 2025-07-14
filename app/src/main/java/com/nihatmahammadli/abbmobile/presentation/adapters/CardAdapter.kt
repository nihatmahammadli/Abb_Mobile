package com.nihatmahammadli.abbmobile.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.databinding.ItemAddNewCardBinding
import com.nihatmahammadli.abbmobile.presentation.dashboard.home.model_home.CardData

class CardAdapter(private val cards: List<CardData>,
    private val onCardButtonClick: (position: Int) -> Unit): RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardAdapter.CardViewHolder {
        val binding = ItemAddNewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]
        with(holder.binding) {
            title.text = card.title
            description.text = card.description
            backgroundImage.setBackgroundResource(card.backgroundResId)
            applyBtn.text = card.buttonText
            applyBtn.setOnClickListener {
                onCardButtonClick(position)
            }

            visaCard.visibility = if (card.showVisa) View.VISIBLE else View.GONE

            applyBtn.setOnClickListener {
                onCardButtonClick(position)
            }
        }
    }

    override fun getItemCount() = cards.size

    inner class CardViewHolder(val binding: ItemAddNewCardBinding): RecyclerView.ViewHolder(binding.root)
}
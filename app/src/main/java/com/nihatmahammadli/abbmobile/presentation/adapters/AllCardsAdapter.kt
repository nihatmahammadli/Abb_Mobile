package com.nihatmahammadli.abbmobile.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nihatmahammadli.abbmobile.databinding.ItemAllCardsBinding
import com.nihatmahammadli.abbmobile.presentation.model.AllCardInfo

class AllCardsAdapter(private val cards: List<AllCardInfo>) : RecyclerView.Adapter<AllCardsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllCardsAdapter.ViewHolder {
        val binding = ItemAllCardsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllCardsAdapter.ViewHolder, position: Int) {
        val card = cards[position]
        with(holder.binding){
            title.text = card.title
            priceText.text = card.priceText
            price.text = card.price
            currency.text = card.currency
            currencyText.text = card.currencyText
            withDrawal.text = card.withDrawal
            withDrawalText.text = card.withDrawalText
            welcomeMiles.text = card.welcomeMiles
            welcomeMilesText.text = card.welcomeMilesText
            tariff.text = card.tariff
            tariffText.text = card.tariffText
            transfer.text = card.transfer
            transferText.text = card.transferText

            Glide.with(holder.itemView.context)
                .load(card.cardImage)
                .into(allCards)
        }
    }

    fun getItem(position: Int): AllCardInfo? {
        return if (position in cards.indices) cards[position] else null
    }

    override fun getItemCount(): Int { return cards.size }

    inner class ViewHolder(val binding: ItemAllCardsBinding): RecyclerView.ViewHolder(binding.root)

}
package com.nihatmahammadli.abbmobile.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.databinding.ItemAddNewCardBinding
import com.nihatmahammadli.abbmobile.databinding.ItemCustomCardBinding
import com.nihatmahammadli.abbmobile.presentation.model.BaseCardData

class CardAdapter(
    private val cards: MutableList<BaseCardData>,
    private val onCardButtonClick: (position: Int) -> Unit,
    private val onCustomCardClick: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_DEFAULT = 0
        private const val TYPE_CUSTOM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (cards[position].isCustom) TYPE_CUSTOM else TYPE_DEFAULT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_CUSTOM -> {
                val binding = ItemCustomCardBinding.inflate(inflater, parent, false)
                CustomCardViewHolder(binding)
            }

            else -> {
                val binding = ItemAddNewCardBinding.inflate(inflater, parent, false)
                DefaultCardViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val card = cards[position]
        when (holder) {
            is DefaultCardViewHolder -> {
                if (card is BaseCardData.DefaultCard) {
                    with(holder.binding) {
                        title.text = card.title
                        description.text = card.description
                        backgroundImage.setBackgroundResource(card.backgroundResId)
                        applyBtn.text = card.buttonText
                        visaCard.visibility = if (card.showVisa) View.VISIBLE else View.GONE

                        applyBtn.setOnClickListener {
                            onCardButtonClick(position)
                        }
                    }
                }
            }

            is CustomCardViewHolder -> {
                if (card is BaseCardData.CustomCard) {
                    with(holder.binding) {
                        title.text = card.title
                        balance.text = card.balance
                        cardCodeEnding.text = card.cardCodeEnding
                        cardExpiryDate.text = card.expiryDate
                        backgroundImage.setBackgroundResource(card.backgroundResId)
                        currentCardImage.setBackgroundResource(card.cardLogoResId)
                        currentCardImage.visibility = if (card.showVisa) View.VISIBLE else View.GONE

                        topUpBtn.setOnClickListener { card.onTopUpClick?.invoke() }
                        payBtn.setOnClickListener { card.onPayClick?.invoke() }
                        transferBtn.setOnClickListener { card.onTransferClick?.invoke() }

                        root.setOnClickListener {
                            onCustomCardClick(position)
                        }
                    }
                }
            }
        }
    }


    fun getCardAt(position: Int): BaseCardData? {
        return cards.getOrNull(position)
    }

    override fun getItemCount(): Int = cards.size

    fun updateItems(newItems: List<BaseCardData>) {
        cards.clear()
        cards.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class DefaultCardViewHolder(val binding: ItemAddNewCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class CustomCardViewHolder(val binding: ItemCustomCardBinding) :
        RecyclerView.ViewHolder(binding.root)
}


package com.nihatmahammadli.abbmobile.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.databinding.ItemAiTextBinding
import com.nihatmahammadli.abbmobile.databinding.ItemUserMessageBinding
import com.nihatmahammadli.abbmobile.presentation.model.ChatMessage
import androidx.recyclerview.widget.DiffUtil

class ChatAdapter(private val messages: MutableList<ChatMessage>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding = ItemUserMessageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            UserViewHolder(binding)
        } else {
            val binding = ItemAiTextBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            AiViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = messages[position]
        if (holder is UserViewHolder) holder.bind(msg)
        else if (holder is AiViewHolder) holder.bind(msg)
    }

    override fun getItemCount() = messages.size

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    class UserViewHolder(private val binding: ItemUserMessageBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(msg: ChatMessage) {
            binding.userMessage.text = msg.text
        }
    }

    class AiViewHolder(private val binding: ItemAiTextBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(msg: ChatMessage) {
            binding.aiMessage.text = msg.text
        }
    }
}

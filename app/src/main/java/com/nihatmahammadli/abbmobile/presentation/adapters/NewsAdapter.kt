package com.nihatmahammadli.abbmobile.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nihatmahammadli.abbmobile.databinding.ItemNewsBinding
import com.nihatmahammadli.abbmobile.presentation.model.NewsItem

class NewsAdapter(val newsList: List<NewsItem>): RecyclerView.Adapter<NewsAdapter.NewsItemHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsAdapter.NewsItemHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsItemHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsAdapter.NewsItemHolder, position: Int) {
        holder.bind(newsList[position])

    }

    override fun getItemCount(): Int {
        return newsList.size
    }
    class NewsItemHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: NewsItem){
            Glide.with(binding.root)
                .load(item.image)
                .into(binding.ForNewsItem)
        }
    }

}
package com.nihatmahammadli.abbmobile.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nihatmahammadli.abbmobile.databinding.ItemRecyclerHomepageBinding

class HorizontalImageAdapter(val imageList: List<Int>) : RecyclerView.Adapter<HorizontalImageAdapter.ImageViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HorizontalImageAdapter.ImageViewHolder {
        val binding = ItemRecyclerHomepageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HorizontalImageAdapter.ImageViewHolder,
        position: Int
    ) {
        val image = imageList[position]
        holder.binding.images.setImageResource(image)
    }

    override fun getItemCount(): Int {return imageList.size
    }
    inner class ImageViewHolder(val binding: ItemRecyclerHomepageBinding) : RecyclerView.ViewHolder(binding.root){
    }
}
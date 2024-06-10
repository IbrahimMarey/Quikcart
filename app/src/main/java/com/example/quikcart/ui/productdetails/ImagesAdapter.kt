package com.example.quikcart.ui.productdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikcart.databinding.ImagesDetailsItemBinding
import com.example.quikcart.models.entities.ImagesItem

class ImagesAdapter : ListAdapter<ImagesItem, ImagesAdapter.ImageViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ImagesDetailsItemBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ImageViewHolder(private val binding: ImagesDetailsItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImagesItem) {
            binding.image = item
            binding.executePendingBindings()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ImagesItem>() {
        override fun areItemsTheSame(oldItem: ImagesItem, newItem: ImagesItem): Boolean {
            return oldItem.src == newItem.src
        }

        override fun areContentsTheSame(oldItem: ImagesItem, newItem: ImagesItem): Boolean {
            return oldItem == newItem
        }
    }
}

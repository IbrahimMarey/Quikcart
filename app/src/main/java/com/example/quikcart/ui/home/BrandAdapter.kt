package com.example.quikcart.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.quikcart.databinding.BrandItemBinding
import com.example.quikcart.models.entities.SmartCollectionsItem

class BrandAdapter: ListAdapter<SmartCollectionsItem, BrandAdapter.BrandViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BrandItemBinding.inflate(inflater,parent,false)
        return BrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BrandViewHolder(private val binding: BrandItemBinding) : ViewHolder(binding.root) {
        fun bind(brandItem:SmartCollectionsItem){
            binding.brandItem = brandItem
            binding.executePendingBindings()
        }

    }

     class DiffCallback: DiffUtil.ItemCallback<SmartCollectionsItem>() {
        override fun areItemsTheSame(
            oldItem: SmartCollectionsItem,
            newItem: SmartCollectionsItem
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: SmartCollectionsItem,
            newItem: SmartCollectionsItem
        ): Boolean {
            return oldItem == newItem
        }

    }
}
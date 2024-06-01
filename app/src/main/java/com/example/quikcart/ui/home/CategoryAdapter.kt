package com.example.quikcart.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.quikcart.databinding.MainCategoryItemBinding
import com.example.quikcart.models.entities.CategoryItem

class CategoryAdapter(private val onItemClick:(CategoryItem) -> Unit): ListAdapter<CategoryItem, CategoryAdapter.CategoryViewHolder>(DiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainCategoryItemBinding.inflate(inflater,parent,false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onItemClick.invoke(getItem(position))
        }
    }
    class CategoryViewHolder(private val itemBinding: MainCategoryItemBinding):ViewHolder(itemBinding.root){
        fun bind(categoryItem: CategoryItem){
            itemBinding.categoryItem = categoryItem
        }
    }

    class DiffUtil: ItemCallback<CategoryItem>() {
        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem == newItem
        }

    }


}
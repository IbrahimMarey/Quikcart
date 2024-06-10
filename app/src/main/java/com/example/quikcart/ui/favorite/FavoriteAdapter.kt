package com.example.quikcart.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikcart.databinding.FavoriteItemBinding
import com.example.quikcart.models.entities.ProductsItem

class FavoriteAdapter(private val itemClickListener: (ProductsItem) -> Unit , private val itemDeleteListener: (ProductsItem) -> Unit) : ListAdapter<ProductsItem, FavoriteAdapter.ProductsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FavoriteItemBinding.inflate(inflater, parent, false)
        return ProductsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickListener , itemDeleteListener)
    }

    class ProductsViewHolder(private val binding: FavoriteItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ProductsItem,
            itemClickListener: (ProductsItem) -> Unit,
            itemDeleteListener: (ProductsItem) -> Unit
        ) {
            binding.product = item
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                itemClickListener(item)
            }
            binding.deleteItem.setOnClickListener{
                itemDeleteListener(item)
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<ProductsItem>() {
        override fun areItemsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
            return oldItem == newItem
        }

    }
}

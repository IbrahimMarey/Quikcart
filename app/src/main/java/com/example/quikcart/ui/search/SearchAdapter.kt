package com.example.quikcart.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikcart.databinding.SearchItemBinding
import com.example.quikcart.models.entities.ProductsItem

class SearchAdapter(
    private val itemClickListener: (ProductsItem) -> Unit,
    private val addToFavoriteClickListener: (ProductsItem) -> Unit
) : ListAdapter<ProductsItem, SearchAdapter.ProductsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchItemBinding.inflate(inflater, parent, false)
        return ProductsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickListener, addToFavoriteClickListener)
    }

    class ProductsViewHolder(private val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ProductsItem,
            itemClickListener: (ProductsItem) -> Unit,
            addToFavoriteClickListener: (ProductsItem) -> Unit
        ) {
            binding.product = item
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                itemClickListener(item)
            }
            binding.addToFavorite.setOnClickListener {
                addToFavoriteClickListener(item)
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<ProductsItem>() {
        override fun areItemsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
            return oldItem == newItem
        }

    }
}

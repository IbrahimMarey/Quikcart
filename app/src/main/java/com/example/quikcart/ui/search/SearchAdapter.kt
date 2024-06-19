package com.example.quikcart.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikcart.R
import com.example.quikcart.databinding.SearchItemBinding
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.utils.setPrice

class SearchAdapter(
    private val itemClickListener: (ProductsItem) -> Unit,
    private val addToFavoriteClickListener: (ProductsItem, Int) -> Unit
) : ListAdapter<ProductsItem, SearchAdapter.ProductsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchItemBinding.inflate(inflater, parent, false)
        return ProductsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickListener, addToFavoriteClickListener, position)
    }

    class ProductsViewHolder(private val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ProductsItem,
            itemClickListener: (ProductsItem) -> Unit,
            addToFavoriteClickListener: (ProductsItem, Int) -> Unit,
            position: Int
        ) {
            binding.product = item
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                itemClickListener(item)
            }
            binding.addToFavorite.setOnClickListener {
                addToFavoriteClickListener(item, position)
            }
            binding.productPrice.setPrice(item.variants?.get(0)?.price?.toFloat() ?: 0.0f, itemView.context)

            if (item.isFavorited) {
                binding.addToFavorite.setImageResource(R.drawable.ic_heart)
            } else {
                binding.addToFavorite.setImageResource(R.drawable.ic_empty_heart)
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

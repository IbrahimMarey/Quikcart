package com.example.quikcart.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.quikcart.R
import com.example.quikcart.databinding.ProductItemBinding
import com.example.quikcart.models.entities.ProductsItem

class ProductAdapter(private val itemClickListener: (ProductsItem) -> Unit,
                      private val addClickListener: (ProductsItem,Int) -> Unit, ) :
    ListAdapter<ProductsItem, ProductAdapter.ProductsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductItemBinding.inflate(inflater, parent, false)
        return ProductsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(getItem(position), itemClickListener , addClickListener , position)
    }

    class ProductsViewHolder(val binding: ProductItemBinding) : ViewHolder(binding.root) {

        fun bind(
            productItem: ProductsItem,
            itemClickListener: (ProductsItem) -> Unit,
            addClickListener: (ProductsItem , Int) -> Unit,
            position: Int

        ) {
            binding.productItem = productItem
            binding.root.setOnClickListener {
                itemClickListener(productItem)
            }
            binding.emptyHeart.setOnClickListener {
                addClickListener(productItem , position)
            }

            if (productItem.isFavorited) {
                binding.emptyHeart.setImageResource(R.drawable.ic_heart)
            } else {
                binding.emptyHeart.setImageResource(R.drawable.ic_empty_heart)
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
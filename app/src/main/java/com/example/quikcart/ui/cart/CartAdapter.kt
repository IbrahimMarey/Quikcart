package com.example.quikcart.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikcart.databinding.CartItemAddedBinding
import com.example.quikcart.models.entities.cart.DraftOrder
import com.example.quikcart.utils.ImageUtils
import com.example.quikcart.utils.setPrice

class CartAdapter(private val delAddress:(DraftOrder)->Unit) : ListAdapter<DraftOrder, CartAdapter.ViewHolder>(DiffUtils) {

    private lateinit var ctx : Context
    class ViewHolder(val binding: CartItemAddedBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        ctx = parent.context
        val binding = CartItemAddedBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val model = getItem(position)
        holder.binding.cartItemTitle.text = model.lineItems[0].title
        holder.binding.cartItemSubTitle.text = model.lineItems[0].quantity.toString()
        holder.binding.cartItemPrice.setPrice(model.totalPrice.toFloat(),ctx)
        model.appliedDiscount?.description?.let {
            ImageUtils.loadImage(holder.binding.cartItemImage,
                it
            )
        }
        holder.binding.btnRemoveItem.setOnClickListener {
            delAddress(model)
        }

    }


    object DiffUtils : DiffUtil.ItemCallback<DraftOrder>() {
        override fun areItemsTheSame(oldItem: DraftOrder, newItem: DraftOrder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DraftOrder, newItem: DraftOrder): Boolean {
            return oldItem.id == newItem.id
        }

    }
}
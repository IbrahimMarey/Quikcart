package com.example.quikcart.ui.cart

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikcart.R
import com.example.quikcart.databinding.CartItemAddedBinding
import com.example.quikcart.models.entities.cart.DraftOrder
import com.example.quikcart.models.entities.cart.LineItem
import com.example.quikcart.utils.ImageUtils
import com.example.quikcart.utils.setPrice

class CartAdapter(private val delCartItem:(LineItem)->Unit, private val editItem :(LineItem, Int,Float) -> Unit) : ListAdapter<LineItem, CartAdapter.ViewHolder>(DiffUtils) {

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
        holder.binding.cartItemTitle.text = model.title
        holder.binding.cartItemPrice.setPrice(model.price.toFloat()*model.quantity,ctx)
        holder.binding.cartItemQuntity.text = model.quantity.toString()
        ImageUtils.loadImage(holder.binding.cartItemImage,model.fulfillmentService)
        holder.binding.btnRemoveItem.setOnClickListener {
            delCartItem(model)
        }
        holder.binding.btnIncrease.setOnClickListener {
            if(model.quantity<=5)
            {
                ++model.quantity
                var newPrice = model.price.toFloat()*model.quantity
                holder.binding.cartItemPrice.setPrice(newPrice,ctx)
                holder.binding.cartItemQuntity.text = model.quantity.toString()
                editItem(model,position,model.price.toFloat())
                holder.binding.btnDecrease.setBackgroundResource(R.drawable.cart_item_ic_bg)

            }else{
                holder.binding.btnIncrease.setBackgroundResource(R.drawable.item_bg_gray)
            }
        }
        holder.binding.btnDecrease.setOnClickListener {
            if (model.quantity>1)
            {
                val price = model.price.toFloat() / model.quantity.toFloat()
                --model.quantity
                var newPrice = model.price.toFloat()*model.quantity
                holder.binding.cartItemPrice.setPrice(newPrice,ctx)
                holder.binding.cartItemQuntity.text = model.quantity.toString()
                editItem(model,position,-model.price.toFloat())
                holder.binding.btnIncrease.setBackgroundResource(R.drawable.cart_item_ic_bg)
            }else{
                holder.binding.btnDecrease.setBackgroundResource(R.drawable.item_bg_gray)
            }
        }

    }


    object DiffUtils : DiffUtil.ItemCallback<LineItem>() {
        override fun areItemsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
            return oldItem.id == newItem.id
        }

    }
}
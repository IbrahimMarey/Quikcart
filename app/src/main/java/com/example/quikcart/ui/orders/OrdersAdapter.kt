package com.example.quikcart.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.quikcart.R
import com.example.quikcart.databinding.OrderItemBinding
import com.example.quikcart.models.entities.OrdersItem

class OrdersAdapter:ListAdapter<OrdersItem,OrdersAdapter.OrdersViewHolder>(DiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OrderItemBinding.inflate (inflater,parent,false)
        return OrdersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OrdersViewHolder(private val itemBinding: OrderItemBinding):ViewHolder(itemBinding.root){
        fun bind(ordersItem: OrdersItem){
            itemBinding.orderItem = ordersItem
            itemBinding.executePendingBindings()
        }
    }

    class DiffUtils:DiffUtil.ItemCallback<OrdersItem>(){
        override fun areItemsTheSame(oldItem: OrdersItem, newItem: OrdersItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: OrdersItem, newItem: OrdersItem): Boolean {
            return oldItem == newItem
        }

    }


}
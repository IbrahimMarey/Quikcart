package com.example.quikcart.ui.orderdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikcart.databinding.OrderItemDetailsBinding
import com.example.quikcart.models.entities.cart.LineItem
import com.example.quikcart.ui.orderdetails.OrderDetailsAdapter.OrderDetailsViewHolder

class OrderDetailsAdapter:ListAdapter<LineItem,OrderDetailsViewHolder>(DiffUtils()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OrderItemDetailsBinding.inflate(inflater,parent,false)
        return OrderDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    class OrderDetailsViewHolder(private val itemBinding:OrderItemDetailsBinding):RecyclerView.ViewHolder(itemBinding.root){
        fun bind(lineItemsItem: LineItem){
            itemBinding.lineItemsItem=lineItemsItem
        }
    }
    class DiffUtils:DiffUtil.ItemCallback<LineItem>(){
        override fun areItemsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: LineItem, newItem: LineItem): Boolean {
            return oldItem == newItem
        }

    }


}
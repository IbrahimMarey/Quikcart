package com.example.quikcart.ui.orderdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikcart.databinding.OrderItemDetailsBinding
import com.example.quikcart.models.entities.LineItemsItem
import com.example.quikcart.ui.orderdetails.OrderDetailsAdapter.OrderDetailsViewHolder

class OrderDetailsAdapter:ListAdapter<LineItemsItem,OrderDetailsViewHolder>(DiffUtils()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OrderItemDetailsBinding.inflate(inflater,parent,false)
        return OrderDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    class OrderDetailsViewHolder(private val itemBinding:OrderItemDetailsBinding):RecyclerView.ViewHolder(itemBinding.root){
        fun bind(lineItemsItem: LineItemsItem){
            itemBinding.lineItemsItem=lineItemsItem
        }
    }
    class DiffUtils:DiffUtil.ItemCallback<LineItemsItem>(){
        override fun areItemsTheSame(oldItem: LineItemsItem, newItem: LineItemsItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: LineItemsItem, newItem: LineItemsItem): Boolean {
            return oldItem == newItem
        }

    }


}
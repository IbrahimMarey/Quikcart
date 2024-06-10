package com.example.quikcart.ui.placeorder.firstscreen

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikcart.R
import com.example.quikcart.databinding.AddressItemBinding
import com.example.quikcart.models.entities.AddressResponse


class OrderCustomerAddressesAdapter(private val onItemClick:(AddressResponse)->Unit) :
    ListAdapter<AddressResponse, OrderCustomerAddressesAdapter.ViewHolder>(DiffUtils) {
        var selectedPosition=-1
    var lastSelectedPosition=-1

    class ViewHolder(val binding: AddressItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(address: AddressResponse) {
            binding.addressItem = address
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
        if (selectedPosition == holder.adapterPosition) {
            holder.binding.cardItem.setCardBackgroundColor(Color.LTGRAY)
            changeBorderItemColor(holder,R.color.xd_text_view_details)
            onItemClick.invoke(getItem(position))
        } else {
            holder.binding.cardItem.setCardBackgroundColor(Color.WHITE)
            changeBorderItemColor(holder, com.paypal.pyplcheckout.R.color.amp_light_gray)
        }

    }

    private fun changeBorderItemColor(holder: ViewHolder,colorId:Int) {
        holder.binding.cardItem.strokeColor =
            holder.binding.cardItem.context.resources.getColor(colorId)
    }

    object DiffUtils : DiffUtil.ItemCallback<AddressResponse>() {
        override fun areItemsTheSame(oldItem: AddressResponse, newItem: AddressResponse): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: AddressResponse,
            newItem: AddressResponse
        ): Boolean {
            return oldItem == newItem
        }

    }
}
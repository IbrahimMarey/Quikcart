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
        private var selectedPosition=0
        private var lastSelectedPosition=0

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
            changeCardItemStyle(holder,Color.LTGRAY,R.color.xd_burgundy)
            onItemClick.invoke(getItem(position))
        } else {
            changeCardItemStyle(holder,Color.WHITE, R.color.xd_btn_nav_item)
        }

    }

    private fun changeCardItemStyle(
        holder: ViewHolder,
        cardBackground: Int,
        cardStrokeColor: Int
    ) {
        holder.binding.cardItem.setCardBackgroundColor(cardBackground)
        changeBorderItemColor(holder,cardStrokeColor)

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
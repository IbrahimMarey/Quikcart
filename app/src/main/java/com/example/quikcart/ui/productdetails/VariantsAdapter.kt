package com.example.quikcart.ui.productdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikcart.databinding.VariantsDetailsItemBinding
import com.example.quikcart.models.entities.VariantsItem

class VariantsAdapter : ListAdapter<VariantsItem, VariantsAdapter.VariantViewHolder>(DiffCallback()) {
    private var selectedPosition=0
    private var lastSelectedPosition=0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = VariantsDetailsItemBinding.inflate(inflater, parent, false)
        return VariantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VariantViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
        if (selectedPosition == holder.adapterPosition) {
            holder.binding.icDone.visibility = View.VISIBLE
        } else {
            holder.binding.icDone.visibility = View.GONE
        }
    }

    class VariantViewHolder(val binding: VariantsDetailsItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: VariantsItem) {
            binding.variant = item
            binding.executePendingBindings()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<VariantsItem>() {
        override fun areItemsTheSame(oldItem: VariantsItem, newItem: VariantsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VariantsItem, newItem: VariantsItem): Boolean {
            return oldItem == newItem
        }
    }
}

package com.example.quikcart.ui.adresses

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikcart.databinding.AddressesItemBinding
import com.example.quikcart.utils.getMarkerAddress
import com.example.quikcart.models.entities.AddressResponse

class AddressesAdapter(private val delAddress:(AddressResponse)->Unit) : ListAdapter<AddressResponse,AddressesAdapter.ViewHolder>(DiffUtils) {

    private lateinit var ctx :Context
    class ViewHolder(val binding:AddressesItemBinding):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        ctx = parent.context
        val binding = AddressesItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val model = getItem(position)

        holder.binding.addressesItemTitle.text = model.address1
        holder.binding.addressesItemSubTitle.text = model.address2

        holder.binding.btnRemoveItem.setOnClickListener {
            delAddress(model)
        }

    }


    object DiffUtils : DiffUtil.ItemCallback<AddressResponse>() {
        override fun areItemsTheSame(oldItem: AddressResponse, newItem: AddressResponse): Boolean {
            return oldItem.address1 == newItem.address1
        }

        override fun areContentsTheSame(oldItem: AddressResponse, newItem: AddressResponse): Boolean {
            return oldItem.address1 == newItem.address1
        }

    }
}
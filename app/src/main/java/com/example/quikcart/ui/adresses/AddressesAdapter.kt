package com.example.quikcart.ui.adresses

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quikcart.databinding.AddressesItemBinding
import com.example.quikcart.helpers.getMarkerAddress
import com.example.quikcart.models.entities.AddressModel

class AddressesAdapter(private val delAddress:(AddressModel)->Unit) : ListAdapter<AddressModel,AddressesAdapter.ViewHolder>(DiffUtils) {

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

        holder.binding.addressesItemTitle.text = model.title
        holder.binding.addressesItemSubTitle.text = getMarkerAddress(ctx,model.lat,model.lng)

        holder.binding.btnRemoveItem.setOnClickListener {
            delAddress(model)
        }

    }


    object DiffUtils : DiffUtil.ItemCallback<AddressModel>() {
        override fun areItemsTheSame(oldItem: AddressModel, newItem: AddressModel): Boolean {
            return oldItem.lat == newItem.lat && oldItem.lng == newItem.lng
        }

        override fun areContentsTheSame(oldItem: AddressModel, newItem: AddressModel): Boolean {
            return oldItem.lat == newItem.lat && oldItem.lng == newItem.lng
        }

    }
}
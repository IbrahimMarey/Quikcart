package com.example.quikcart.ui.orderdetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentOrderDetailsBinding
import com.example.quikcart.models.entities.OrdersItem

class OrderDetailsFragment : Fragment() {
    lateinit var binding : FragmentOrderDetailsBinding
    val adapter=OrderDetailsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentOrderDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val orderItem=OrderDetailsFragmentArgs.fromBundle(requireArguments()).orderItem
        Log.e("TAG", "onViewCreated: $orderItem", )
        initRecyclerView(orderItem)
    }

    private fun initRecyclerView(orderItem: OrdersItem) {
        adapter.submitList(orderItem.lineItems)
        binding.orderDetailsRecycler.adapter=adapter
    }


}
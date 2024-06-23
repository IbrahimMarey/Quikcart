package com.example.quikcart.ui.orderdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.quikcart.databinding.FragmentOrderDetailsBinding
import com.example.quikcart.models.entities.OrdersItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsFragment : Fragment() {
    lateinit var binding: FragmentOrderDetailsBinding
    private var adapter =OrderDetailsAdapter()
    lateinit var orderItem: OrdersItem


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPassedArgs()
        initRecyclerView(orderItem)
    }

    private fun getPassedArgs() {
        orderItem = OrderDetailsFragmentArgs.fromBundle(requireArguments()).orderItem
    }

    private fun initRecyclerView(orderItem: OrdersItem) {
        adapter.submitList(orderItem.lineItems)
        binding.orderDetailsRecycler.adapter = adapter
    }
}
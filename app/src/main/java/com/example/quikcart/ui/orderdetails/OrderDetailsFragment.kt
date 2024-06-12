package com.example.quikcart.ui.orderdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentOrderDetailsBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.OrdersItem
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.utils.AlertUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
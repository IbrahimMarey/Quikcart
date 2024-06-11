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
import com.example.quikcart.databinding.FragmentOrderDetailsBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.OrdersItem
import com.example.quikcart.utils.AlertUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderDetailsFragment : Fragment() {
    lateinit var binding: FragmentOrderDetailsBinding
    val adapter = OrderDetailsAdapter()
    lateinit var viewModel: OrderDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val orderItem = OrderDetailsFragmentArgs.fromBundle(requireArguments()).orderItem
        initViewModel()
        viewModel.filterProductByTitle(orderItem)
        observeOnStateFlow(orderItem)
    }

    private fun observeOnStateFlow(orderItem: OrdersItem) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { product ->
                    when (product) {
                        is ViewState.Error -> {
                            AlertUtil.showToast(requireContext(), product.message)
                        }
                        ViewState.Loading -> {
                        }
                        is ViewState.Success -> {
                            initRecyclerView(orderItem)
                        }
                    }
                }
            }
        }
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[OrderDetailsViewModel::class]
    }

    private fun initRecyclerView(orderItem: OrdersItem) {
        adapter.submitList(orderItem.lineItems)
        binding.orderDetailsRecycler.adapter = adapter
    }


}
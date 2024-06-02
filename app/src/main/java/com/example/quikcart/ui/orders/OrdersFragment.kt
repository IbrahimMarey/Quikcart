package com.example.quikcart.ui.orders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.quikcart.databinding.FragmentOrdersBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.OrdersItem
import com.example.quikcart.utils.AlertUtil
import com.example.quikcart.utils.PreferencesUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment : Fragment() {
    lateinit var binding : FragmentOrdersBinding
    private lateinit var viewModel: OrdersViewModel
    private lateinit var adapter: OrdersAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        val customerId = PreferencesUtils.getInstance(requireContext()).getCustomerId()
        Log.e("TAG", "onViewCreated: $customerId", )
        viewModel.getCustomerOrders(customerId)
        observeOnStateFlow()
    }

    private fun observeOnStateFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{orders->
                    when(orders){
                        is ViewState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            AlertUtil.showToast(requireContext(),orders.message)
                        }
                        ViewState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is ViewState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            initRecyclerView(orders.data)
                        }
                    }

                }
            }
        }
    }

    private fun initRecyclerView(orders:List<OrdersItem>){
        adapter = OrdersAdapter()
        adapter.submitList(orders)
        binding.ordersRecycler.adapter = adapter
    }

    private fun initViewModel() {
        viewModel=ViewModelProvider(this)[OrdersViewModel::class.java]
    }

}
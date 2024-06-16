package com.example.quikcart.ui.placeorder.secondscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.quikcart.databinding.FragmentPlaceOrderBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.Order
import com.example.quikcart.models.entities.OrdersItem
import com.example.quikcart.models.entities.cart.DraftOrder
import com.example.quikcart.utils.AlertUtil
import com.example.quikcart.utils.PreferencesUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlaceOrderFragment : Fragment() {

    private lateinit var totalPrice: String
    private lateinit var viewModel: PlaceOrderViewModel
    private lateinit var binding: FragmentPlaceOrderBinding
    private lateinit var address: String
    private lateinit var draftOrder: DraftOrder
    @Inject lateinit var preferencesUtils: PreferencesUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        getPassedArgs()
        viewModel.totalPrice = totalPrice
        viewModel.shippingFees = "0"
        val customer = com.example.quikcart.models.entities.Customer(
            email = "sama@gmail.com",
            id = preferencesUtils.getCustomerId(),

            )
        val orderItem = OrdersItem(
            lineItems = draftOrder.lineItems,
            customer = customer, totalPrice = totalPrice,
            totalTax = "0",
            currency = preferencesUtils.getCurrencyType(),
            createdAt = "2025-05-20T15:27:23-04:00",
            paymentGatewayNames = mutableListOf("Cash")
        )
        viewModel.orderResponse = Order(orderItem)
        binding.vm = viewModel
        observeOnLiveData()
        observeOnStateFlow()
    }

    private fun observeOnLiveData() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                AlertUtil.showProgressDialog(requireContext())
            } else {
                AlertUtil.hideProgressDialog()
            }
        }
    }


    private fun observeOnStateFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is ViewState.Error -> {
                            AlertUtil.showToast(requireContext(), it.message)
                        }

                        is ViewState.Success -> {
                            viewModel.deleteCartItemsById(draftOrder.id.toString())
                            AlertUtil.showToast(requireContext(), "Order is placed successfully")
                        }
                        is ViewState.Loading -> {}

                    }
                }
            }
        }
    }

    private fun getPassedArgs() {
        draftOrder = PlaceOrderFragmentArgs.fromBundle(requireArguments()).draftOrder
        totalPrice = PlaceOrderFragmentArgs.fromBundle(requireArguments()).totalPrice
        address = PlaceOrderFragmentArgs.fromBundle(requireArguments()).address
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[PlaceOrderViewModel::class]
    }

}
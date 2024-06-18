package com.example.quikcart.ui.placeorder.secondscreen

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentPlaceOrderBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.models.entities.Customer
import com.example.quikcart.models.entities.Order
import com.example.quikcart.models.entities.OrdersItem
import com.example.quikcart.models.entities.ShippingAddress
import com.example.quikcart.models.entities.cart.DraftOrder
import com.example.quikcart.utils.AlertUtil
import com.example.quikcart.utils.DateUtil
import com.example.quikcart.utils.PaymentMethod
import com.example.quikcart.utils.PreferencesUtils
import com.paypal.pyplcheckout.data.model.pojo.Extensions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MAXIMUM_CASH_AMOUNT=10000
@AndroidEntryPoint
class PlaceOrderFragment : Fragment() {

    private lateinit var totalPrice: String
    private lateinit var viewModel: PlaceOrderViewModel
    private lateinit var binding: FragmentPlaceOrderBinding
    private lateinit var address: AddressResponse
    private lateinit var draftOrder: DraftOrder
    @Inject lateinit var preferencesUtils: PreferencesUtils
    private var paymentMethod=PaymentMethod.CASH

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        getPassedArgs()
        Log.e("TAG", "onViewCreated email: ${preferencesUtils.getCustomerEmail()}", )
        Log.e("TAG", "onViewCreated: ${paymentMethod.name}", )
        initializeViewModelVariables()
        binding.vm = viewModel
        observeOnLiveData()
        observeOnStateFlow()
        Log.e("TAG", "getShippingAddress: ${address.phone}", )
        //checkTotalPrice()
    }

    private fun checkTotalPrice() {
        Log.e("TAG", "checkTotalPrice: ${totalPrice}", )
        binding.cashPayment.visibility=if(totalPrice.toFloat() >= MAXIMUM_CASH_AMOUNT) View.GONE else View.VISIBLE
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeViewModelVariables() {
        viewModel.totalPrice = totalPrice
        viewModel.shippingFees = "0"
        viewModel.orderResponse = Order(getOrderItem())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getOrderItem():OrdersItem {
        return OrdersItem(
            lineItems = draftOrder.lineItems,
            customer = getCustomerData(), totalPrice = totalPrice,
            totalTax = "0",
            currency = preferencesUtils.getCurrencyType(),
            createdAt = DateUtil.getCurrentDateAndTime(),
            paymentGatewayNames = mutableListOf(paymentMethod.name),
            shippingAddress = getShippingAddress()

        )
    }

    private fun getCustomerData():Customer {
        return Customer(
            email = preferencesUtils.getCustomerEmail(),
            id = preferencesUtils.getCustomerId(),
        )
    }

    private fun getShippingAddress():ShippingAddress {
        Log.e("TAG", "getShippingAddress: ${address.phone}", )
        return ShippingAddress(
            country = address.country,
            address1 = address.address1,
            address2 = address.address2,
            city = address.city
        )
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
                           // viewModel.sendEmail("Hello")


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
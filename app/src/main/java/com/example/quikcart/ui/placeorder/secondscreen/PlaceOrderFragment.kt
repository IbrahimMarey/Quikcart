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
import com.example.quikcart.utils.PreferencesUtils
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlaceOrderFragment : Fragment() {

    private lateinit var totalPrice: String
    private lateinit var discountPrice: String
    private lateinit var viewModel: PlaceOrderViewModel
    private lateinit var binding: FragmentPlaceOrderBinding
    private lateinit var address: AddressResponse
    private lateinit var draftOrder: DraftOrder
    @Inject lateinit var preferencesUtils: PreferencesUtils

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
        setUPPayPal(totalPrice)
        Log.e("TAG", "onViewCreated: ${preferencesUtils.getCustomerEmail()}", )
        initializeViewModelVariables()
        binding.vm = viewModel
        observeOnLiveData()
        observeOnStateFlow()
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
            paymentGatewayNames = mutableListOf("Cash"),
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
                            viewModel.sendEmail("Hello")
                        }
                        is ViewState.Loading -> {}

                    }
                }
            }
        }
    }


    private fun getPassedArgs() {
        draftOrder = PlaceOrderFragmentArgs.fromBundle(requireArguments()).draftOrder
        totalPrice = PlaceOrderFragmentArgs.fromBundle(requireArguments()).priceData.total
        discountPrice = PlaceOrderFragmentArgs.fromBundle(requireArguments()).priceData.discount
        address = PlaceOrderFragmentArgs.fromBundle(requireArguments()).address
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[PlaceOrderViewModel::class]
    }

    private fun setUPPayPal(amount : String)
    {
        binding.paymentButtonContainer.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    OrderRequest(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD
                                    , value = "10.0")
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                Log.i("TAG", "OrderId: ${approval.data.orderId}")
                Toast.makeText(requireActivity(), "Payment Approved", Toast.LENGTH_SHORT).show()
            },
            onCancel = OnCancel{
                Log.i("TAG", "onViewCreated: ==================== payment canceld")
                Toast.makeText(requireActivity(), "Payment Cancel", Toast.LENGTH_SHORT).show()

            },
            onError = OnError{
                Log.i("TAG", "onViewCreated: ${it}")
                Toast.makeText(requireActivity(), "Payment Error", Toast.LENGTH_SHORT).show()

            }
        )
    }
}
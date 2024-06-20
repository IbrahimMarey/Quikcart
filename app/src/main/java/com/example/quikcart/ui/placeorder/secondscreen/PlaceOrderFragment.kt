package com.example.quikcart.ui.placeorder.secondscreen

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
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

const val MAXIMUM_CASH_AMOUNT=10000f
// paypal
/*private const val PAYPAL_CLIENT_ID = "AfR2ylX7Lxzx92G30PzuibgSS0tIPLGNlFy0ove_c7tEzoxGjOfGkL0MhMoPHimdP7n-rqPaHGtDGirp"
private const val PAYPAL_REQUEST_CODE = 7171
private val paypalConfig = PayPalConfiguration()
    .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
    .clientId(PAYPAL_CLIENT_ID)*/
@AndroidEntryPoint
class PlaceOrderFragment : Fragment() {

    private lateinit var totalPrice: String
    private lateinit var discountPrice: String
    private lateinit var viewModel: PlaceOrderViewModel
    private lateinit var binding: FragmentPlaceOrderBinding
    private lateinit var address: AddressResponse
    private lateinit var draftOrder: DraftOrder
    @Inject lateinit var preferencesUtils: PreferencesUtils
    private var paymentMethod=PaymentMethod.CASH
    private var counter=0
    private var isPaymentApproved=false
    private var isPayPalChoose=false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceOrderBinding.inflate(inflater, container, false)
        /*binding.paymentButtonContainer.setup(
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
                                    , value = totalPrice)
                            )
                        )
                    )

                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                paymentMethod=PaymentMethod.PAYPAL
                isPayPalChoose=true
                isPaymentApproved=true
                viewModel.confirmOrder()
                Log.d("TAG", "OrderId: ${approval.data.orderId}")
                Toast.makeText(requireActivity(), "Payment Approved", Toast.LENGTH_SHORT).show()
            },
            onCancel = OnCancel{
                isPayPalChoose=true
                isPaymentApproved=false
                Log.i("TAG", "onViewCreated: ==================== payment canceld")
                Toast.makeText(requireActivity(), "Payment Cancel", Toast.LENGTH_SHORT).show()

            },
            onError = OnError{
                isPayPalChoose=true
                isPaymentApproved=false
                Log.i("TAG", "setUPPayPal: ${it}")
                Toast.makeText(requireActivity(), "Payment Error", Toast.LENGTH_SHORT).show()

            }
        )*/
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if(PreferencesUtils.isPayWithPayPal)
        {
            PreferencesUtils.isPayWithPayPal = false
            viewModel.confirmOrder()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        getPassedArgs()
        clickOnCash()
        Log.e("TAG", "onViewCreated email: ${preferencesUtils.getCustomerEmail()}", )
        Log.e("TAG", "onViewCreated: ${paymentMethod.name}", )
        Log.e("TAG", "onViewCreated: ${draftOrder.customer.phone}", )

        initializeViewModelVariables()
        binding.vm = viewModel
        observeOnLiveData()
        observeOnStateFlow()
        Log.e("TAG", "onViewCreated: ${totalPrice}", )
        checkTotalPrice(totalPrice.toFloat())
        changePaymentBackground()
        Log.e("TAG", "getShippingAddress: ${address.phone}", )
        navigateToPayPalPayment()
    }
    private fun changePaymentBackground() {
        binding.paymentLinear.setOnClickListener {
            isPayPalChoose=true
            counter++
            Log.e("TAG", "onViewCreated1: ${counter}",)
            if (counter % 2 == 0) {
                binding.paymentLinear.setBackgroundResource(R.drawable.rounded_green_bg)
                binding.cashPayment.setBackgroundResource(R.drawable.rounded_bg)
            }
            else {
                 isPayPalChoose=false
                binding.cashPayment.setBackgroundResource(R.drawable.rounded_green_bg)
                binding.paymentLinear.setBackgroundResource(R.drawable.rounded_bg)
            }
        }
    }

    private fun navigateToPayPalPayment()
    {
        binding.paypalPayCard.setOnClickListener {
            val amount = "%.2f".format(totalPrice.toFloat() * preferencesUtils.getUSDRate())
            val action = PlaceOrderFragmentDirections.actionPlaceOrderFragmentToPaymentFragment(amount)
            Navigation.findNavController(it).navigate(action)
        }
    }
    private fun clickOnCash()
    {
        binding.placeOredrBtn.setOnClickListener {
            AlertUtil.showCustomAlertDialog(
                requireActivity(),
                "Are You Sure You want To Pay with Cash",
                positiveText = "Confirm",
                positiveClickListener = { _, _ ->
                    viewModel.confirmOrder()
                },
                negText = "Cancel",
                negClickListener = {_,_->
                    AlertUtil.dismissAlertDialog()
                }
                )
        }
    }


    private fun checkTotalPrice(totalPrice:Float) {
        var checkPrice= MAXIMUM_CASH_AMOUNT
        Log.e("TAG", "checkTotalPrice: $checkPrice", )
        Log.e("TAG", "checkTotalPrice: $", )

        binding.cashPayment.visibility=if(totalPrice >= checkPrice.toFloat()) {
            if(preferencesUtils.getCurrencyType() == PreferencesUtils.CURRENCY_USD)
                checkPrice *=preferencesUtils.getUSDRate()
            AlertUtil.showCustomAlertDialog(requireContext(),
                "Note: Cash Not Allowed",
                "When the value of your purchases exceeds ${checkPrice}, you cannot pay using cash","Ok")
            View.GONE
        } else View.VISIBLE
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeViewModelVariables() {
        viewModel.totalPrice = totalPrice
        viewModel.shippingFees = "0"
        viewModel.discount=discountPrice
        viewModel.maximumCashAmount= MAXIMUM_CASH_AMOUNT.toString()
        viewModel.subTotal=(totalPrice.toFloat() + discountPrice.toFloat()).toString()
        viewModel.isPayPalChoose=isPayPalChoose
        viewModel.isPaymentApproved=isPaymentApproved
        viewModel.orderResponse = Order(getOrderItem())

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getOrderItem():OrdersItem {
        return OrdersItem(
            lineItems = draftOrder.lineItems,
            customer = getCustomerData(), totalPrice = totalPrice,
            totalTax = "0",
            currency = preferencesUtils.getCurrencyType(),
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
                            AlertUtil.showToast(requireContext(), it.message) }

                        is ViewState.Success -> {
                            viewModel.deleteCartItemsById(draftOrder.id.toString())
                            AlertUtil.showToast(requireContext(), "Order is placed successfully")
                            preferencesUtils.setCartId(0)
                            findNavController().popBackStack(R.id.homeFragment, false)
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
        totalPrice = PlaceOrderFragmentArgs.fromBundle(requireArguments()).priceData.total
        discountPrice = PlaceOrderFragmentArgs.fromBundle(requireArguments()).priceData.discount
        address = PlaceOrderFragmentArgs.fromBundle(requireArguments()).address
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[PlaceOrderViewModel::class]
    }


/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        paypalActivityResult(requestCode, resultCode, data)
    }

    private fun paypalActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == PAYPAL_REQUEST_CODE){
            if (requestCode == Activity.RESULT_OK)
            {
                var confirmation = data?.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (confirmation != null)
                {
                    var d = confirmation.toJSONObject().toString(4)
                    Log.i("TAG", "onActivityResult: ${d}")
                }
            }else if (requestCode == Activity.RESULT_CANCELED){
                Toast.makeText(requireActivity(), getString(R.string.cancel), Toast.LENGTH_SHORT).show()
            }
        }else if(requestCode == PaymentActivity.RESULT_EXTRAS_INVALID)
        {
            Toast.makeText(requireActivity(), getString(R.string.delete), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPayPalService()
    }
    private fun startPayPalService()
    {
        val intent = Intent(requireActivity(), PayPalService::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig)
        requireActivity().startService(intent)
    }
    private fun stopPayPalService()
    {
        requireActivity().stopService(Intent(requireActivity(),PayPalService::class.java))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopPayPalService()
    }*/
}
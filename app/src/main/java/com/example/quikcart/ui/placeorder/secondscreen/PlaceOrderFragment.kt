package com.example.quikcart.ui.placeorder.secondscreen

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
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
import com.example.quikcart.models.entities.PriceRule
import com.example.quikcart.models.entities.ShippingAddress
import com.example.quikcart.models.entities.cart.DraftOrder
import com.example.quikcart.ui.placeorder.firstscreen.ConfirmOrderFirstScreenViewModel
import com.example.quikcart.utils.AlertUtil
import com.example.quikcart.utils.PaymentMethod
import com.example.quikcart.utils.PreferencesUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder


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
    private lateinit var materialAboutUsBuilder: MaterialAlertDialogBuilder
    private lateinit var secondViewModel:ConfirmOrderFirstScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceOrderBinding.inflate(inflater, container, false)
        materialAboutUsBuilder = MaterialAlertDialogBuilder(requireActivity())
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
        secondViewModel = ViewModelProvider(this)[ConfirmOrderFirstScreenViewModel::class]
        initViewModel()
        getPassedArgs()
        clickOnCash()
        secondViewModel.getCustomerAddresses(preferencesUtils.getCustomerId())

        totalPrice = draftOrder.totalPrice
        discountPrice = "0"
        applyCoupon()
        initializeViewModelVariables()
        binding.vm = viewModel
        observeOnLiveData()
        observeOnStateFlow()
        checkTotalPrice(totalPrice.toFloat())
        changePaymentBackground()
        getAddresses()
    }
    private fun changePaymentBackground() {
        /*binding.payment_method_rg.setOnCheckedChangeListener {
            isPayPalChoose=true
            counter++
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
        */
        binding.paymentMethodRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.cash_payment -> {
                    isPayPalChoose=true
                }
                R.id.paypalPayCard -> {
                    isPayPalChoose=false
                }
            }
        }
    }

    private fun checkPayment(){
        if (binding.paymentMethodRg.checkedRadioButtonId == R.id.cash_payment)
        {
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
        else if (binding.paymentMethodRg.checkedRadioButtonId == R.id.paypalPayCard){
            navigateToPayPalPayment()
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
            checkPayment()
        }
    }
    private fun checkTotalPrice(totalPrice:Float) {
        var checkPrice= MAXIMUM_CASH_AMOUNT
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

        viewModel.shippingFees = "50"
        viewModel.totalPrice = (totalPrice.toFloat() + 50.0f).toString()
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
            customer = getCustomerData(),
            totalPrice = totalPrice,
            totalTax = discountPrice.toFloat().minus(50.0f).toString(),
            currency = preferencesUtils.getCurrencyType(),
            paymentGatewayNames = mutableListOf(paymentMethod.name),
            shippingAddress = getShippingAddress(),
            currentTotalDiscounts = discountPrice

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
                            AlertUtil.showSnackbar(requireView(), it.message) }

                        is ViewState.Success -> {
                            viewModel.deleteCartItemsById(draftOrder.id.toString())
                            AlertUtil.showSnackbar(requireView(), "Order is placed successfully")
                            preferencesUtils.setCartId(0)
                            findNavController().popBackStack(R.id.homeFragment, false)
                            showAnimationAfterConfirmOrder()
                            // viewModel.sendEmail("Hello")

                        }
                        is ViewState.Loading -> {}

                    }
                }
            }
        }
    }

    private fun showAnimationAfterConfirmOrder()
    {
        val confirmOrderAnimationDialog = layoutInflater.inflate(R.layout.confirm_order_dialog, null)

        val alertDialog = materialAboutUsBuilder.setView(confirmOrderAnimationDialog)
            .setBackground(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.dialogue_background, requireActivity().theme
                )
            ).setCancelable(true).show()

        confirmOrderAnimationDialog.findViewById<Button>(R.id.closeAnimationOrder).setOnClickListener {
            alertDialog.cancel()
        }
    }

    private fun getPassedArgs() {
        draftOrder = PlaceOrderFragmentArgs.fromBundle(requireArguments()).draftOrder
    /*    totalPrice = (PlaceOrderFragmentArgs.fromBundle(requireArguments()).priceData.total )
        draftOrder.totalPrice = totalPrice
        discountPrice = PlaceOrderFragmentArgs.fromBundle(requireArguments()).priceData.discount
        address = PlaceOrderFragmentArgs.fromBundle(requireArguments()).address
*/
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[PlaceOrderViewModel::class]
    }

    // applyCoupon
    private fun checkCoupon(coupon:String)
    {
        val priceRule = secondViewModel.couponsList.find { it.id.toString() == coupon }
        if (priceRule != null)
        {
            applyCoupon(priceRule)
        }else{
            showMSG(getString(R.string.coupon_is_not_found),)
        }
    }

    private fun applyCoupon(priceRule: PriceRule)
    {
        var price = totalPrice.toFloat()
        if (priceRule.valueType == "percentage")
        {
            var percentageAmount = (price/100)*priceRule.value.toFloat()
            price += percentageAmount
            discountPrice = (totalPrice.toFloat() - price).toString()
            totalPrice = price.toString()
            showMSG(getString(R.string.coupon_confirmed))
        }else
        {
            totalPrice = (totalPrice.toFloat() + priceRule.value.toFloat()).toString()
            discountPrice = priceRule.value
            showMSG(getString(R.string.coupon_confirmed))
        }
        lockCouponsViews()
    }

    private fun lockCouponsViews()
    {
        binding.validateBtn.isEnabled = false
        binding.couponField.isEnabled = false
        binding.textLayoutCoupon.isEnabled =false
    }

    private fun showMSG(msg:String){
        AlertUtil.showSnackbar(requireView(),msg)
    }
    private fun applyCoupon(){
        val coupon = binding.couponField.text.toString()
        binding.validateBtn.setOnClickListener {
            checkCoupon(coupon)
        }
    }
    private fun selectAddress(name:String , address:String){
        binding.userName.text = name
        binding.address.text = address
    }
    private fun getAddresses(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                secondViewModel.uiState.collect {
                    when (it) {
                        is ViewState.Error ->{
                     //       binding.progressBar.visibility = View.GONE
                            AlertUtil.showSnackbar(requireView(), it.message)}
                        is ViewState.Success -> {
                            val address = it.data
                            val myAddress = address[0].city+","+ address[0].country.toString()
                            selectAddress(address[0].first_name.toString() , myAddress)
                         //   initAddressesRecycler(it.data)
                         //   binding.progressBar.visibility = View.GONE
                        }
                        is ViewState.Loading -> {}
                           // binding.progressBar.visibility = View.VISIBLE
                    }

                }

            }

        }
    }
}
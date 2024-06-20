package com.example.quikcart.ui.placeorder.firstscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentConfirmOrderFirstScreenBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.models.entities.PriceRule
import com.example.quikcart.models.entities.cart.DraftOrder
import com.example.quikcart.models.entities.cart.TotalAndDiscountModel
import com.example.quikcart.ui.placeorder.firstscreen.ConfirmOrderFirstScreenFragmentArgs
import com.example.quikcart.utils.AlertUtil
import com.example.quikcart.utils.PreferencesUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmOrderFirstScreenFragment : Fragment() ,Navigator{
    lateinit var binding: FragmentConfirmOrderFirstScreenBinding
    lateinit var viewModel: ConfirmOrderFirstScreenViewModel
    lateinit var adapter: OrderCustomerAddressesAdapter
    @Inject lateinit var preferenceManager: PreferencesUtils
    private lateinit var totalPrice : String
    private lateinit var discountPrice : String
    private lateinit var selectedAddress:AddressResponse
    private lateinit var draftOrder: DraftOrder
    private lateinit var totalAndDiscountModel: TotalAndDiscountModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentConfirmOrderFirstScreenBinding.inflate(inflater,container,false)
        draftOrder=ConfirmOrderFirstScreenFragmentArgs.fromBundle(requireArguments()).draftOrder
        totalPrice = draftOrder.totalPrice
        discountPrice = "0"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        binding.vm=viewModel
        viewModel.navigator=this
        viewModel.getCustomerAddresses(preferenceManager.getCustomerId())
        observeOnStateFlow()
        observeOnLiveData()
        binding.validateBtn.setOnClickListener {
            val coupon = binding.couponField.text.toString()
            checkCoupon(coupon)
        }
    }

    private fun observeOnLiveData() {
        viewModel.phoneState.observe(viewLifecycleOwner) { msg ->
            AlertUtil.showToast(requireContext(),msg)
        }
    }

    private fun observeOnStateFlow(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect {
                    when (it) {
                        is ViewState.Error ->{
                            binding.progressBar.visibility = View.GONE
                            AlertUtil.showToast(requireContext(), it.message)}
                        is ViewState.Success -> {
                            initAddressesRecycler(it.data)
                            binding.progressBar.visibility = View.GONE
                        }
                        is ViewState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    }

                }

            }

        }
    }
    private fun initAddressesRecycler(addresses: List<AddressResponse>) {
        adapter= OrderCustomerAddressesAdapter {address->
            selectedAddress=address
        }
        adapter.submitList(addresses)
        binding.addressRecycler.adapter=adapter
    }

    private fun initViewModel() {
         viewModel=ViewModelProvider(this)[ConfirmOrderFirstScreenViewModel::class]
    }

    private fun checkCoupon(coupon:String)
    {
        val priceRule = viewModel.couponsList.find { it.id.toString() == coupon }
        if (priceRule != null)
        {
            applyCoupon(priceRule)
        }else{
            Toast.makeText(requireActivity(), getString(R.string.coupon_is_not_found), Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireActivity(), getString(R.string.coupon_confirmed), Toast.LENGTH_SHORT).show()
        }else
        {
            totalPrice = (totalPrice.toFloat() + priceRule.value.toFloat()).toString()
            discountPrice = priceRule.value
            Toast.makeText(requireActivity(), getString(R.string.coupon_confirmed), Toast.LENGTH_SHORT).show()
        }
        lockCouponsViews()
    }

    private fun lockCouponsViews()
    {
        binding.validateBtn.isEnabled = false
        binding.couponField.isEnabled = false
        binding.textLayoutCoupon.isEnabled =false
    }

    override fun navigateToMapFragment() {
        findNavController().navigate(R.id.action_confirmOrderFirstScreenFragment_to_mapFragment)
    }

    override fun navigateToConfirmOrderFragment() {
        val totalAndDiscountModel = TotalAndDiscountModel(totalPrice,discountPrice)
        if(::selectedAddress.isInitialized){
            val action = ConfirmOrderFirstScreenFragmentDirections.actionConfirmOrderFirstScreenFragmentToPlaceOrderFragment(selectedAddress,draftOrder,totalAndDiscountModel)
            findNavController().navigate(action)

        }else{
            AlertUtil.showCustomAlertDialog(requireContext(),
                "Please add your Address","Ok")
        }
    }
}
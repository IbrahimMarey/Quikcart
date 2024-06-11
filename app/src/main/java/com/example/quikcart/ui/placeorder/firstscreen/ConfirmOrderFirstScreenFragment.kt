package com.example.quikcart.ui.placeorder.firstscreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentConfirmOrderFirstScreenBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.models.entities.PriceRule
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentConfirmOrderFirstScreenBinding.inflate(inflater,container,false)
        totalPrice = ConfirmOrderFirstScreenFragmentArgs.fromBundle(requireArguments()).totalPrice
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        binding.vm=viewModel
        viewModel.navigator=this
        viewModel.getCustomerAddresses(preferenceManager.getCustomerId())
        observeOnStateFlow()
        binding.continueBtn.setOnClickListener {
            val action = ConfirmOrderFirstScreenFragmentDirections.actionConfirmOrderFirstScreenFragmentToPlaceOrderFragment(totalPrice)
            Navigation.findNavController(it).navigate(action)
//            findNavController().navigate(R.id.action_confirmOrderFirstScreenFragment_to_placeOrderFragment)
        }

        binding.validateBtn.setOnClickListener {
            val coupon = binding.couponField.text.toString()
            checkCoupon(coupon)
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
            Log.e("TAG", "initAddressesRecycler: ${address.address1}, address2: ${address.address2}," +
                    "cit: ${address.city}, county: ${address.country}, countyName: ${address.country_name}," +
                    "province: ${address.province}" +
                    "name: ${address.name}", )
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
            totalPrice = price.toString()
            Toast.makeText(requireActivity(), getString(R.string.coupon_confirmed), Toast.LENGTH_SHORT).show()
        }else
        {
            totalPrice = (totalPrice.toFloat() + priceRule.value.toFloat()).toString()
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
}
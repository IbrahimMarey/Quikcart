package com.example.quikcart.ui.placeorder.firstscreen

import android.content.SharedPreferences
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
import androidx.navigation.fragment.findNavController
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentConfirmOrderFirstScreenBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.utils.AlertUtil
import com.example.quikcart.utils.PreferencesUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmOrderFirstScreenFragment : Fragment() {
    lateinit var binding: FragmentConfirmOrderFirstScreenBinding
    lateinit var viewModel: ConfirmOrderFirstScreenViewModel
    lateinit var adapter: OrderCustomerAddressesAdapter
    @Inject lateinit var preferenceManager: PreferencesUtils



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentConfirmOrderFirstScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        viewModel.getCustomerAddresses(preferenceManager.getCustomerId())
        observeOnStateFlow()
        binding.continueBtn.setOnClickListener {
            findNavController().navigate(R.id.action_confirmOrderFirstScreenFragment_to_placeOrderFragment)
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
}
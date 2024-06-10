package com.example.quikcart.ui.adresses

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quikcart.R
import com.example.quikcart.databinding.FragmentAddressesBinding
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.AddressModel
import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.utils.PreferencesUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddressesFragment : Fragment() {
    lateinit var binding:FragmentAddressesBinding
    private lateinit var addressesViewModel: AddressesViewModel
    private lateinit var adapter:AddressesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressesBinding.inflate(inflater,container,false)
        addressesViewModel = ViewModelProvider(this)[AddressesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addAddressBtn.setOnClickListener {
            val action = AddressesFragmentDirections.actionAddressesFragmentToMapFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        addressesViewModel.getCustomerAddresses(PreferencesUtils.getInstance(requireContext()).getUserId()
            ?.toLong()?: "7406457553131".toLong())
        setUpUI()
    }
    private fun setUpUI()
    {
        binding.recyclerAddresses.visibility= View.GONE
        binding.notFoundAddresses.visibility = View.GONE
        val delAction :(AddressResponse)->Unit= {
            delAddress(it)
        }
        adapter = AddressesAdapter(delAction)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerAddresses.adapter = adapter
        binding.recyclerAddresses.layoutManager = layoutManager
        setUpAddressesData()
    }

    private fun setUpAddressesData()
    {
        lifecycleScope.launch(Dispatchers.Main) {
            addressesViewModel.customerAddresses.collectLatest {
                when(it)
                {
                    is ViewState.Loading->{

                    }
                    is ViewState.Success->{
                        if(it.data.isEmpty())
                        {
                            binding.notFoundAddresses.visibility = View.VISIBLE
                            binding.recyclerAddresses.visibility = View.GONE
                            binding.loadingBar.visibility = View.GONE
                        }else{
                            adapter.submitList(it.data)
                            binding.recyclerAddresses.visibility = View.VISIBLE
                            binding.loadingBar.visibility = View.GONE
                            binding.notFoundAddresses.visibility = View.GONE

                        }
                    }
                    is ViewState.Error->{
                        binding.notFoundAddresses.visibility = View.VISIBLE
                        binding.recyclerAddresses.visibility = View.GONE
                        binding.loadingBar.visibility = View.GONE
                    }
                }
            }
        }
    }
    private fun delAddress(addressesModel:AddressResponse){
        Snackbar.make(requireView(), getString(R.string.are_you_sure_you_want_to_delete_this_address), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.delete)) {
                addressesViewModel.delCustomerAddress(PreferencesUtils.getInstance(requireContext()).getCustomerId(),addressesModel.id)
            }.show()
    }
}
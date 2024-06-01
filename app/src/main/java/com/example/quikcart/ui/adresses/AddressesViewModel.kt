package com.example.quikcart.ui.adresses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.AddressModel
import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.models.entities.PostAddressModel
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressesViewModel @Inject constructor(private val _appRepo:Repository):ViewModel() {
    private val _customerAddresses :MutableStateFlow<ViewState<List<AddressResponse>>> = MutableStateFlow(ViewState.Loading)
    val customerAddresses : StateFlow<ViewState<List<AddressResponse>>> = _customerAddresses

    init {

    }


    fun postAddress(customerID:Long,addressResponse: PostAddressModel){
        viewModelScope.launch(Dispatchers.IO) {
            _appRepo.postAddressShopify(customerID,addressResponse)
        }
        getCustomerAddresses(customerID)
    }

    fun getCustomerAddresses(customerID:Long)
    {
        viewModelScope.launch {
            _appRepo.getAllAddressesShopify(customerID).catch {
                Log.d("TAG", "getCustomerAddresses: ${it.message}")
            }.collect{
                _customerAddresses.value = ViewState.Success(it.addresses)
            }
        }
    }

    fun delCustomerAddress(customerID:Long,id:Long)
    {
        viewModelScope.launch {
            _appRepo.delAddressShopify(customerID,id)
        }
        getCustomerAddresses(customerID)
    }
}
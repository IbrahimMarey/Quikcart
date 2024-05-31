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
        getCustomerAddresses()
    }


    fun postAddress(addressResponse: PostAddressModel){
        viewModelScope.launch(Dispatchers.IO) {
            _appRepo.postAddressShopify(addressResponse)
        }
        getCustomerAddresses()
    }

    fun getCustomerAddresses()
    {
        viewModelScope.launch {
            _appRepo.getAllAddressesShopify().catch {
                Log.d("TAG", "getCustomerAddresses: ${it.message}")
            }.collect{
                _customerAddresses.value = ViewState.Success(it.addresses)
            }
        }
    }

    fun delCustomerAddress(id:Long)
    {
        viewModelScope.launch {
            _appRepo.delAddressShopify(id)
        }
        getCustomerAddresses()
    }
}
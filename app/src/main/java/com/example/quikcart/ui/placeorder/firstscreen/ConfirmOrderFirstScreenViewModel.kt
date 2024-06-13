package com.example.quikcart.ui.placeorder.firstscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.models.entities.PriceRule
import com.example.quikcart.models.entities.cart.LineItem
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmOrderFirstScreenViewModel@Inject constructor(private val repo: Repository):ViewModel() {
    lateinit var navigator: Navigator
    private val _uiState : MutableStateFlow<ViewState<List<AddressResponse>>> = MutableStateFlow(
        ViewState.Loading)
    val uiState : StateFlow<ViewState<List<AddressResponse>>> = _uiState
    private val _couponsState : MutableStateFlow<ViewState<List<PriceRule>>> = MutableStateFlow(
        ViewState.Loading)
    val couponsState : StateFlow<ViewState<List<PriceRule>>> = _couponsState
    var couponsList:List<PriceRule> = listOf()

    init {
        getAllCoupons()
    }
    fun getCustomerAddresses(customerID:Long)
    {
        viewModelScope.launch {
            _uiState.value=ViewState.Loading
            repo.getAllAddressesShopify(customerID).catch {
                _uiState.value = it.localizedMessage?.let { it1 -> ViewState.Error(it1) }!!
            }.collect{
                _uiState.value = ViewState.Success(it.addresses)
            }
        }
    }

    fun navigateToConfirmOrderFragment(){
        navigator.navigateToConfirmOrderFragment()
    }

    fun navigateToMapFragment(){
        navigator.navigateToMapFragment()
    }

    private fun getAllCoupons()
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllCoupons().collect{
                _couponsState.value = ViewState.Success(it)
                couponsList = it
                Log.i("TAG", "getAllCoupons: = = = = = = = = = = = = = = = = = = = $it")
            }
        }
    }

}
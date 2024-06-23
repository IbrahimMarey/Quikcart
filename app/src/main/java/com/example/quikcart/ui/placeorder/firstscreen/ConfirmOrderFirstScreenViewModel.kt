package com.example.quikcart.ui.placeorder.firstscreen

import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denzcoskun.imageslider.models.SlideModel
import com.example.quikcart.R
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.models.entities.PostAddressModel
import com.example.quikcart.models.entities.PriceRule
import com.example.quikcart.models.entities.cart.LineItem
import com.example.quikcart.models.repos.Repository
import com.example.quikcart.utils.getMarkerAddress
import com.example.quikcart.utils.isEgyptianPhoneNumberValid
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
    var phone:String?=null

    private val _phoneState = MutableLiveData<String>()
    val phoneState: LiveData<String> = _phoneState


    init {
        getAllCoupons()
    }
    fun getCustomerAddresses(customerID:Long)
    {
        viewModelScope.launch {
            _uiState.value=ViewState.Loading
            repo.getAllAddressesShopify(customerID).catch {
                _uiState.value = it.localizedMessage?.let { it1 -> ViewState.Error("something error: $it1") }!!
            }.collect{
                _uiState.value = ViewState.Success(it.addresses)
            }
        }
    }

    private fun isValidatePhone():Boolean{
        val isValidate: Boolean
        if(phone?.isNotEmpty() == true || phone?.isNotBlank() == true){
            if (phone?.let { isEgyptianPhoneNumberValid(it) } == true)
            {
                isValidate=true
            }else
            { _phoneState.postValue("Phone miss match")
                isValidate=false
            }
        } else{
            _phoneState.postValue("Please enter your phone")
            isValidate=false
        }
        return isValidate
    }
    fun navigateToConfirmOrderFragment(){
        if(isValidatePhone()){
            phone?.let { navigator.navigateToConfirmOrderFragment(it) }
        }
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
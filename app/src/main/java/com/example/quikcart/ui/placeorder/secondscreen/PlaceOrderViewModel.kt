package com.example.quikcart.ui.placeorder.secondscreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.Order
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceOrderViewModel @Inject constructor(private val repo: Repository) : ViewModel() {
    lateinit var totalPrice: String
    lateinit var shippingFees: String
    lateinit var orderResponse: Order
    private val _uiState = MutableStateFlow<ViewState<Order>>(ViewState.Loading)
    var uiState: StateFlow<ViewState<Order>> = _uiState

    private val _isLoading=MutableLiveData(false)
    var isLoading:LiveData<Boolean> =_isLoading

    fun confirmOrder() {
        viewModelScope.launch {
            _isLoading.value=true
            _uiState.value = ViewState.Loading
            repo.confirmOrder(orderResponse).catch {
                _isLoading.value=false
                _uiState.value = it.localizedMessage?.let { it1 -> ViewState.Error(it1) }!!
            }.collect { orderItems ->
                _isLoading.value=false
                _uiState.value = ViewState.Success(orderItems)
            }
        }
    }


    fun deleteCartItemsById(id:String){
        viewModelScope.launch {
            while (true){
                try {
                    repo.delCartItem(id)
                    break
                }catch (ex:Exception){
                    Log.e("TAG", "deleteCartItemsById: ${ex.localizedMessage}", )
                }
            }

        }
    }
}
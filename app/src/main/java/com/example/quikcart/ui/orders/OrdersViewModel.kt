package com.example.quikcart.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.OrdersItem
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(private val repo:Repository):ViewModel() {
    private val _uiState = MutableStateFlow<ViewState<List<OrdersItem>>>(ViewState.Loading)
    var uiState:StateFlow<ViewState<List<OrdersItem>>> = _uiState
    fun getCustomerOrders(customerId:Long){
        viewModelScope.launch {
            _uiState.value = ViewState.Loading
            repo.getCustomerOrders(customerId)
                .catch {error->
                    _uiState.value = error.localizedMessage?.let { ViewState.Error(it) }!!
                }.collect{orders->
                    _uiState.value = ViewState.Success(orders)
                }
        }
    }
}
package com.example.quikcart.ui.orderdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.OrdersItem
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(private val repo: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow<ViewState<ProductsItem>>(ViewState.Loading)
    var uiState:StateFlow<ViewState<ProductsItem>> = _uiState
    fun filterProductByTitle(ordersItem: OrdersItem) {
        viewModelScope.launch {
            ordersItem.lineItems?.forEach { itemLine ->
                repo.getProducts().catch {
                    _uiState.value= it.localizedMessage?.let { it1 -> ViewState.Error(it1) }!!
                }.collect { products ->
                   products.forEach {
                       if(it.title == itemLine?.title) {
                           itemLine?.image=it.image
                           _uiState.value= ViewState.Success(it)
                           return@forEach
                       }
                   }
                }
            }
        }
    }
}
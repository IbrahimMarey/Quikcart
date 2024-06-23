package com.example.quikcart.ui.orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.OrdersItem
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(private val repo: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow<ViewState<List<OrdersItem>>>(ViewState.Loading)
    var uiState: StateFlow<ViewState<List<OrdersItem>>> = _uiState
    lateinit var totalPrice:String


    fun getCustomerOrders(customerId: Long) {
        viewModelScope.launch {
            _uiState.value = ViewState.Loading
            try {
                val orders = repo.getCustomerOrders(customerId).first()
                val products = repo.getProducts().first()

                val productsMap=products.associateBy { it.title }
                orders.forEach { orderItem ->

                    Log.e("TAG", "getCustomerOrders: ${orderItem.currentTotalDiscounts}", )
                    filterProductByTitle(orderItem, productsMap)
                }

                _uiState.value = ViewState.Success(orders)
            } catch (e: Exception) {
                _uiState.value = e.localizedMessage?.let { ViewState.Error("server down, please try again later: $it") }!!
            }
        }
    }

    private fun filterProductByTitle(
        ordersItem: OrdersItem,
        productsMap: Map<String?, ProductsItem>
    ) {
        ordersItem.lineItems?.forEach { itemLine ->
            val product = productsMap[itemLine?.title]
            if (product != null) {
                itemLine?.image = product.image
            }
        }
    }
}

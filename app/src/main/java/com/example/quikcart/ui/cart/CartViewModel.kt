package com.example.quikcart.ui.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.models.entities.cart.CartResponse
import com.example.quikcart.models.entities.cart.DraftOrder
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repo: Repository) : ViewModel()
{
    private val _cartItems : MutableStateFlow<ViewState<List<DraftOrder>>> = MutableStateFlow(
        ViewState.Loading)
    val cartItems : StateFlow<ViewState<List<DraftOrder>>> = _cartItems

    init {
        getAllCartItems()
    }

    private fun getAllCartItems()
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCartItems().collect{
                _cartItems.value = ViewState.Success(it.draftOrders)
            }
        }
    }

    fun delCartItem(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.delCartItem(id)
            getAllCartItems()
        }
    }
}
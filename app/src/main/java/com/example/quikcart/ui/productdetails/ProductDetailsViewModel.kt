package com.example.quikcart.ui.productdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.entities.cart.PostCartItemModel
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val repo: Repository) : ViewModel() {

    fun postProductInCart(cartItem:PostCartItemModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.postCartItem(cartItem).collect{
                Log.i("TAG", "\n\n\n\npostProductInCart: ==============================  $it")
            }
        }
    }
}
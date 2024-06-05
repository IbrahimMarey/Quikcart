package com.example.quikcart.ui.products

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val repo: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow<ViewState<List<ProductsItem>>>(ViewState.Loading)
    var uiState: StateFlow<ViewState<List<ProductsItem>>> = _uiState

    fun getProductsByBrandId(id: Long) {
        viewModelScope.launch {
            _uiState.value = ViewState.Loading
            repo.getProductsByBrandId(id).catch { error ->
                _uiState.value = error.localizedMessage?.let { ViewState.Error(it) }!!
            }.collect { products ->
                getPriceForEachProduct(products)
                _uiState.value = ViewState.Success(products)
            }
        }
    }

     fun getProductsBySubCategory(mainCategory: String,subCategory: String){
         val productsOfCategory : MutableList<ProductsItem> = mutableListOf()
        viewModelScope.launch {
            _uiState.value = ViewState.Loading
            repo.getProductsBySubCategory(subCategory).catch {error->
                _uiState.value = error.localizedMessage?.let { ViewState.Error(it) }!!
            }.collect{products->
              products.forEach {product->

                  if(product.tags?.contains(mainCategory) == true){
                      productsOfCategory.add(product)
                      Log.e("TAG", "getProductsBySubCategory:not found${mainCategory} ", )

                  }
                  else{
                      Log.e("TAG", "getProductsBySubCategory:not found${mainCategory} ", )
                  }
              }

                getPriceForEachProduct(productsOfCategory)
                _uiState.value = ViewState.Success(productsOfCategory)
            }
        }

    }

    private fun getPriceForEachProduct(products: List<ProductsItem>) {
        products.forEach { item ->
            item.variants?.forEach {
                item.price = it.price
            }
        }
    }
    fun addToFavourites(product: ProductsItem) {
        viewModelScope.launch {
            repo.inertProduct(product)
        }
    }
}
package com.example.quikcart.ui.products

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.repos.Repository
import com.google.android.material.slider.Slider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class ProductsViewModel @Inject constructor(private val repo: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow<ViewState<List<ProductsItem>>>(ViewState.Loading)
    var uiState: StateFlow<ViewState<List<ProductsItem>>> = _uiState
    private var originalProducts: List<ProductsItem> = emptyList()
    var minPrice=ObservableField(0)
    var maxPrice=ObservableField(1)

    fun getProductsByBrandId(id: Long) {
        viewModelScope.launch {
            _uiState.value = ViewState.Loading
            repo.getProductsByBrandId(id).catch { error ->
                Log.e("TAG", "error: ${error.localizedMessage}", )
                _uiState.value = error.localizedMessage?.let { ViewState.Error(it) }!!
            }.collect { products ->
                originalProducts=products
                getPriceForEachProduct(products)
                getMaxPrice(products)
                getMinPrice(products)
                Log.e("TAG", "MAX: ${maxPrice.get()}", )
                Log.e("TAG", "MIN: ${minPrice.get()}", )
                _uiState.value = ViewState.Success(products)
            }
        }
    }

    private fun getMinPrice(products: List<ProductsItem>) {
        if(products.size<=1){
            minPrice.set(0)
            return
        }
        minPrice.set(products.minByOrNull { product->
            product.price?.toDouble() ?: Double.MAX_VALUE
        }?.price?.toDouble()?.toInt() ?: 0)
    }

    private fun getMaxPrice(products: List<ProductsItem>) {
        if(products.isEmpty()){
            maxPrice.set(1)
            return
        }
        maxPrice.set(products.maxByOrNull { product->
            product.price?.toDouble() ?: Double.MAX_VALUE
        }?.price?.toDouble()?.toInt() ?: 1)
    }

    fun getProductsBySubCategory(mainCategory: String,subCategory: String){
         val productsOfCategory : MutableList<ProductsItem> = mutableListOf()
        viewModelScope.launch {
            _uiState.value = ViewState.Loading
            repo.getProductsBySubCategory(subCategory).catch {error->
                _uiState.value = error.localizedMessage?.let { ViewState.Error(it) }!!
            }.collect{products->
                originalProducts=products
              products.forEach {product->
                  filterProductByMainCategory(product,mainCategory,productsOfCategory)
              }
                getPriceForEachProduct(productsOfCategory)
                getMaxPrice(productsOfCategory)
                getMinPrice(productsOfCategory)
                Log.e("TAG", "MAX2: ${maxPrice.get()}", )
                Log.e("TAG", "MIN2: ${minPrice.get()}", )
                _uiState.value = ViewState.Success(productsOfCategory)
            }
        }

    }

    private fun filterProductByMainCategory(
        product: ProductsItem,
        mainCategory: String,
        productsOfCategory: MutableList<ProductsItem>
    ) {
        if(product.tags?.contains(mainCategory) == true){
            productsOfCategory.add(product)
            Log.e("TAG", "getProductsBySubCategory:not found${mainCategory} ", )
        }
    }

    private fun getPriceForEachProduct(products: List<ProductsItem>) {
        products.forEach { item ->
            item.variants?.forEach {
                item.price = it.price
            }
        }
    }

     fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
         var filteredProducts: List<ProductsItem>
         viewModelScope.launch {
              filteredProducts = originalProducts.filter {
                 it.price!=null && it.price?.toFloatOrNull()!! <= value
             }
             _uiState.value=ViewState.Success(filteredProducts)
         }
    }

    fun addToFavourites(product: ProductsItem) {
        viewModelScope.launch {
            repo.inertProduct(product)
        }
    }
}
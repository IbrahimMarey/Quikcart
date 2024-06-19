package com.example.quikcart.ui.search

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.cart.*
import com.example.quikcart.models.repos.Repository
import com.google.android.material.slider.Slider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repo: Repository) : ViewModel() {
    private val _uiState = MutableStateFlow<ViewState<List<ProductsItem>>>(ViewState.Loading)
    var uiState: StateFlow<ViewState<List<ProductsItem>>> = _uiState

    private var lineItemsList:MutableList<LineItem> = mutableListOf()
    var originalProducts: List<ProductsItem> = emptyList()
    var minPrice= ObservableField(0)
    var maxPrice= ObservableField(1)

    private val _favOperationState = MutableStateFlow<ViewState<Unit>>(ViewState.Loading)


    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            _uiState.value = ViewState.Loading
            val favoriteProducts = repo.getAllProducts().firstOrNull() ?: emptyList()
            repo.getProducts().catch { error ->
                _uiState.value = error.localizedMessage?.let { ViewState.Error(it) }!!
            }.collect { productsItem ->
                originalProducts=productsItem
                getPriceForEachProduct(productsItem)
                getMinPrice(productsItem)
                getMaxPrice(productsItem)
                val productsWithFavorites = productsItem.map { product ->
                    if (favoriteProducts.any { it.id == product.id }) {
                        product.copy(isFavorited = true)
                    } else {
                        product
                    }
                }
                _uiState.value = ViewState.Success(productsWithFavorites)
                Log.e("TAG", "getProduct: ${productsWithFavorites[0].title}")
            }
        }
    }
    fun addToFavourites(productsItem: ProductsItem) {
        viewModelScope.launch {
            repo.inertProduct(productsItem)
        }
    }

    fun putProductInFav(id: String, cartItem: PutDraftOrderItemModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.putDraftOrder(id, cartItem).catch { error ->
                _favOperationState.value = ViewState.Error(error.localizedMessage ?: "Unknown Error")
            }.collect {
                _favOperationState.value = ViewState.Success(Unit)
            }
        }
    }

    suspend fun postProductInFav(cartItem: PostDraftOrderItemModel): Long {
        return withContext(Dispatchers.IO) {
            var draftOrderId: Long = 0
            repo.postDraftOrder(cartItem).catch { error ->
                _favOperationState.value = ViewState.Error(error.localizedMessage ?: "Unknown Error")
            }.collect { result ->
                draftOrderId = result.draft_order.id!!
                _favOperationState.value = ViewState.Success(Unit)
            }
            draftOrderId
        }
    }

    fun getFav(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDraftOrderById(id).catch { error ->
                _favOperationState.value = ViewState.Error(error.localizedMessage ?: "Unknown Error")
            }.collect {
                it.draft_order.lineItems?.let { it1 -> lineItemsList.addAll(it1) }
                _favOperationState.value = ViewState.Success(Unit)
            }
        }
    }

    fun getItemLineList(itemTitle: String, itemPrice: String): List<DraftOrderLineItem> {
        val draftOrderLineList: MutableList<DraftOrderLineItem> = mutableListOf()
        var draftOrderLineItem = DraftOrderLineItem(itemTitle, itemPrice, 1)
        draftOrderLineList.add(draftOrderLineItem)
        for (item in lineItemsList) {
            draftOrderLineItem = DraftOrderLineItem(item.title, item.price, item.quantity.toInt())
            draftOrderLineList.add(draftOrderLineItem)
        }
        return draftOrderLineList
    }
    
    fun onValueChange(value: Float) {
        var filteredProducts: List<ProductsItem>
        viewModelScope.launch {
            filteredProducts = originalProducts.filter {
                it.price!=null && it.price?.toFloatOrNull()!! <= value
            }
            _uiState.value=ViewState.Success(filteredProducts)
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
    private fun getPriceForEachProduct(products: List<ProductsItem>) {
        products.forEach { item ->
           if(item.variants?.isNotEmpty() == true){
               item.price=item.variants[0].price
           }
        }
    }

}

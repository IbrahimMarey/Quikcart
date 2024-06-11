package com.example.quikcart.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.cart.DraftOrderLineItem
import com.example.quikcart.models.entities.cart.LineItem
import com.example.quikcart.models.entities.cart.PostDraftOrderItemModel
import com.example.quikcart.models.entities.cart.PutDraftOrderItemModel
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repo: Repository) :ViewModel() {
    private val _uiState = MutableStateFlow<ViewState<List<ProductsItem>>>(ViewState.Loading)
    var uiState: StateFlow<ViewState<List<ProductsItem>>> = _uiState
    private var lineItemsList:MutableList<LineItem> = mutableListOf()

    fun getProducts() {
         viewModelScope.launch {
             _uiState.value = ViewState.Loading
             repo.getProducts().catch {
                  error->
                 _uiState.value = error.localizedMessage?.let { ViewState.Error(it) }!!
             }
                 .collect {productsItem->
                     _uiState.value = ViewState.Success(productsItem)
                     Log.e("TAG", "getProduct: ${productsItem[0].title}", )
                 }
         }
     }

    fun addToFavourites(productsItem: ProductsItem) {
        viewModelScope.launch {
            repo.inertProduct(productsItem)
        }
    }
    fun putProductInFav(id:String, cartItem: PutDraftOrderItemModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.putDraftOrder(id ,cartItem).collect{
            }
        }
    }

    suspend fun postProductInFav(cartItem: PostDraftOrderItemModel): Long {
        return withContext(Dispatchers.IO) {
            var draftOrderId: Long = 0
            repo.postDraftOrder(cartItem).collect { result ->
                draftOrderId = result.draft_order.id
            }
            draftOrderId
        }
    }
    fun getFav(id: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDraftOrderById(id).collect{
                lineItemsList.addAll(it.draft_order.lineItems)
            }
        }
    }
    fun getItemLineList(itemTitle:String , itemPrice:String):List<DraftOrderLineItem>
    {
        val draftOrderLineList:MutableList<DraftOrderLineItem> = mutableListOf()
        var draftOrderLineItem = DraftOrderLineItem(itemTitle,itemPrice,1)
        draftOrderLineList.add(draftOrderLineItem)
        for (item in lineItemsList)
        {
            draftOrderLineItem = DraftOrderLineItem(item.title,item.price,item.quantity.toInt())
            draftOrderLineList.add(draftOrderLineItem)
        }
        return draftOrderLineList
    }

}
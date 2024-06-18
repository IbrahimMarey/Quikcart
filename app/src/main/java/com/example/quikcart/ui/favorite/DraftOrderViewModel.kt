package com.example.quikcart.ui.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.cart.DraftOrder
import com.example.quikcart.models.entities.cart.DraftOrderLineItem
import com.example.quikcart.models.entities.cart.LineItem
import com.example.quikcart.models.entities.cart.PutDraftItem
import com.example.quikcart.models.entities.cart.PutDraftOrderItemModel
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val IMAGE_NOT_FOUND = "https://i.vimeocdn.com/video/443809727-02de8fe8dfa0df7806ebceb3e9e52991f3ff640b12ddabbb06051f04d1af765f-d_640?f=webp"
@HiltViewModel
class DraftOrderViewModel @Inject constructor(private val repo: Repository) : ViewModel() {
    private val _cart: MutableStateFlow<ViewState<DraftOrder>> = MutableStateFlow(ViewState.Loading)
    val cart: StateFlow<ViewState<DraftOrder>> = _cart
    var lineItemsList: MutableList<LineItem> = mutableListOf()
    private val _uiState = MutableStateFlow<ViewState<List<ProductsItem>>>(ViewState.Loading)
    var uiState: StateFlow<ViewState<List<ProductsItem>>> = _uiState

     suspend fun delFavItem(id: String , item: LineItem) {
        val data = PutDraftOrderItemModel(PutDraftItem(
                getLineItemsFormDraftOrderLineItem(lineItemsList)
            )
        )
        editFav(id, data)
    }

    fun getFav(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDraftOrderById(id)
                .catch { e ->
                    "Error fetching draft order".handleFetchError(e)
                }
                .collect {
                    _cart.value = ViewState.Success(it.draft_order)
                    lineItemsList.addAll(it.draft_order.lineItems)
                    logDraftOrderItems(it.draft_order.lineItems)
                }
        }
    }

    private fun logDraftOrderItems(items: List<LineItem>) {
        items.forEach { item ->
            Log.d("DraftOrderItem", "Item: ${item.title}, Price: ${item.price}, Quantity: ${item.quantity}")
        }
    }

    private suspend fun editFav(id: String, cartItem: PutDraftOrderItemModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.putDraftOrder(id ,cartItem).collect{

            }
           getFav(id)
        }
    }

    private fun getLineItemsFormDraftOrderLineItem(items: List<LineItem>): List<DraftOrderLineItem> {
        val lineItemsList: MutableList<DraftOrderLineItem> = mutableListOf()
        for (it in items) {
            val draftOrderLineItem = DraftOrderLineItem(it.title, it.price, it.quantity.toInt())
            lineItemsList.add(draftOrderLineItem)
        }
        return lineItemsList
    }

    fun delFav(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
                repo.delCartItem(id)
                _cart.value = ViewState.Error("Not Found")
        }
    }

    private fun String.handleFetchError(e: Throwable) {
        _cart.value = ViewState.Error(this)
        Log.e("DraftOrderViewModel", "${this}: ${e.message}")
    }

}

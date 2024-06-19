package com.example.quikcart.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
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
import kotlinx.coroutines.withContext
import javax.inject.Inject
private const val IMAGE_NOT_FOUND = "https://i.vimeocdn.com/video/443809727-02de8fe8dfa0df7806ebceb3e9e52991f3ff640b12ddabbb06051f04d1af765f-d_640?f=webp"
@HiltViewModel
class CartViewModel @Inject constructor(private val repo: Repository) : ViewModel()
{
    private val _cart : MutableStateFlow<ViewState<DraftOrder>> = MutableStateFlow(
        ViewState.Loading)
    val cart : StateFlow<ViewState<DraftOrder>> = _cart
    var lineItemsList:MutableList<LineItem> = mutableListOf()
    fun delCartItem(id:String,item:LineItem){
        lineItemsList.remove(item)
        val data =PutDraftOrderItemModel(PutDraftItem(
            getLineItemsFormDraftOrderLineItem(lineItemsList)
        ))
        editCart(id,data)
    }

    fun saveCartWhileLeaving(id:String){
        val data =PutDraftOrderItemModel(PutDraftItem(
            getLineItemsFormDraftOrderLineItem(lineItemsList)
        ))
        editCart(id,data)
    }
    fun getCart(id: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDraftOrderById(id)
                .catch {
                    _cart.value = ViewState.Error(it.message ?: "Error")
                }
                .collect{
                    lineItemsList.clear()
                    lineItemsList.addAll(getProducts(it.draft_order.lineItems))
                    getProducts(it.draft_order.lineItems)
                    _cart.value = ViewState.Success(it.draft_order)
            }
        }
    }
    private fun editCart(id:String,cartItem: PutDraftOrderItemModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.putDraftOrder(id ,cartItem).collect{
            }
            getCart(id)
        }
    }
    private fun getLineItemsFormDraftOrderLineItem(items:List<LineItem>):List<DraftOrderLineItem>{
        var lineItemsList:MutableList<DraftOrderLineItem> = mutableListOf()
        for (it in items)
        {
            var draftOrderLineItem = DraftOrderLineItem(it.title,it.price,it.quantity.toInt())
            lineItemsList.add(draftOrderLineItem)
        }
        return lineItemsList
    }

    fun delCart(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.delCartItem(id)
            _cart.value = ViewState.Error("Not Found")
        }
    }

    suspend fun getProducts(list: List<LineItem>): List<LineItem> {
        val updatedList = mutableListOf<LineItem>()
        withContext(Dispatchers.IO) {
            repo.getProducts().collect { productList ->
                updatedList.addAll(list.map { lineItem ->
                    lineItem.copy(
                        fulfillmentService = productList.firstOrNull { product ->
                            product.title == lineItem.title
                        }?.images?.firstOrNull()?.src ?: IMAGE_NOT_FOUND
                    )
                })
            }
        }
        return updatedList
    }
}
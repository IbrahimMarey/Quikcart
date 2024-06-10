package com.example.quikcart.ui.productdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcart.models.ViewState
import com.example.quikcart.models.entities.cart.DraftOrderLineItem
import com.example.quikcart.models.entities.cart.LineItem
import com.example.quikcart.models.entities.cart.PostDraftOrderItemModel
import com.example.quikcart.models.entities.cart.PutDraftOrderItemModel
import com.example.quikcart.models.repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val repo: Repository) : ViewModel() {

    private var lineItemsList:MutableList<LineItem> = mutableListOf()
    fun putProductInCart(id:String,cartItem: PutDraftOrderItemModel)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.putDraftOrder(id ,cartItem).collect{
            }
        }
    }

    suspend fun postProductInCart(cartItem: PostDraftOrderItemModel): Long {
        return withContext(Dispatchers.IO) {
            var draftOrderId: Long = 0
            repo.postDraftOrder(cartItem).collect { result ->
                draftOrderId = result.draft_order.id
            }
            draftOrderId
        }
    }
    fun getCart(id: String)
    {
        Log.i("TAG", "getCart============================================ $id")
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDraftOrderById(id).collect{
                lineItemsList.addAll(it.draft_order.lineItems)
            }
        }
    }
    fun getItemLineList(itemTitle:String , itemPrice:String):List<DraftOrderLineItem>
    {
        var draftOrderLineList:MutableList<DraftOrderLineItem> = mutableListOf()
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
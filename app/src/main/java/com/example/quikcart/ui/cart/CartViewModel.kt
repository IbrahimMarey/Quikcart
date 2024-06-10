package com.example.quikcart.ui.cart

import android.util.Log
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

//        for (itemLine in lineItemsList)
//        {
//            if(itemLine.id == item.id)
                lineItemsList.remove(item)
//        }
//        lineItemsList.removeIf { it.id == item.id }
//        if (lineItemsList.isNotEmpty()){

            val data =PutDraftOrderItemModel(PutDraftItem(
                getLineItemsFormDraftOrderLineItem(lineItemsList)
            ))
            editCart(id,data)
//        }else
//        {
//
//        }
    }

    fun getCart(id: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDraftOrderById(id)
                .catch {
                    _cart.value = ViewState.Error(it.message ?: "Error")
                }
                .collect{
                _cart.value = ViewState.Success(it.draft_order)
                lineItemsList.addAll(it.draft_order.lineItems)
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

    fun getProducts()
    {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getProducts().collect{


                for(lineItem in lineItemsList)
                {
                    for (product in it) {
                        if (lineItem.title == product.title)
                        {
                            lineItem.fulfillmentService = product.images?.get(0)?.src?: IMAGE_NOT_FOUND
                        }
                    }
                }
            }
        }
    }
}
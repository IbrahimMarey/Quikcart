package com.example.quikcart.models.entities.cart


data class PostDraftOrderItemModel(val draft_order : DraftItem)
data class PutDraftOrderItemModel(val draft_order: PutDraftItem)
data class PutDraftItem(val line_items: List<DraftOrderLineItem> )
data class DraftItem(
    val line_items : List<DraftOrderLineItem>,
    val applied_discount : CartAppliedDiscount?,
    val customer: CartCustomer,
    val use_customer_default_address : Boolean = true)

data class DraftOrderLineItem(
    val title:String,
    val price: String,
    val quantity: Int,
)

data class CartAppliedDiscount(
    val description:String?,
    val value_type:String?,
    val value:String?,
    val amount:String?,
    val title:String?
)

data class CartCustomer(
    val id : Long
)
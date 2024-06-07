package com.example.quikcart.models.entities.cart


data class PostCartItemModel(val draft_order : CartItem)
data class CartItem(val name : String,val line_items : List<CartLineItems>, val applied_discount : CartAppliedDiscount?, val customer: CartCustomer, val use_customer_default_address : Boolean = true)

data class CartLineItems(
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
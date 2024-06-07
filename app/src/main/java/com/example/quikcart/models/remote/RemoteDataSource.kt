package com.example.quikcart.models.remote

import com.example.quikcart.models.entities.AddressesResponse
import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse
import com.example.quikcart.models.entities.Customers

import com.example.quikcart.models.entities.FetchAddress
import com.example.quikcart.models.entities.OrdersItem
import com.example.quikcart.models.entities.PostAddressModel

import com.example.quikcart.models.entities.ProductsItem

import com.example.quikcart.models.entities.SmartCollectionsItem
import com.example.quikcart.models.entities.cart.CartResponse
import com.example.quikcart.models.entities.cart.PostCartItemModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteDataSource {
     fun getBrands():Flow<List<SmartCollectionsItem>>
     fun getProductsByBrandId(id:Long):Flow<List<ProductsItem>>
     fun getProductsBySubCategory(category:String):Flow<List<ProductsItem>>
     suspend fun postCustomer(customerRequest: CustomerRequest): Response<CustomerResponse>

     suspend fun postAddress(customerID:Long,address: PostAddressModel): Response<FetchAddress>
     suspend fun getCustomerAddresses(customerID:Long,): Response<AddressesResponse>

     suspend fun delCustomerAddress(customerID:Long,id : Long)

     fun getProducts():Flow<List<ProductsItem>>
     fun getCustomer():Flow<List<Customers>>
     fun getCustomerOrders(customerID: Long):Flow<List<OrdersItem>>

     suspend fun postCartItem(
          cartItem: PostCartItemModel
     ): Flow<PostCartItemModel>

     suspend fun getCartItems(): Flow<CartResponse>
     suspend fun delCartItem(id : String)
}
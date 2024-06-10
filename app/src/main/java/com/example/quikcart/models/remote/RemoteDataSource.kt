package com.example.quikcart.models.remote

import com.example.quikcart.models.entities.AddressesResponse
import com.example.quikcart.models.entities.CouponModel
import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse
import com.example.quikcart.models.entities.Customers

import com.example.quikcart.models.entities.FetchAddress
import com.example.quikcart.models.entities.OrdersItem
import com.example.quikcart.models.entities.PostAddressModel

import com.example.quikcart.models.entities.ProductsItem

import com.example.quikcart.models.entities.SmartCollectionsItem
import com.example.quikcart.models.entities.cart.AllDraftOrdersResponse
import com.example.quikcart.models.entities.cart.DraftOrderResponse
import com.example.quikcart.models.entities.cart.PostDraftOrderItemModel
import com.example.quikcart.models.entities.cart.PutDraftOrderItemModel
import com.example.quikcart.utils.Constants
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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
          cartItem: PostDraftOrderItemModel
     ): Flow<PostDraftOrderItemModel>

     suspend fun getCartItems(): Flow<AllDraftOrdersResponse>
     suspend fun delCartItem(id : String)
     suspend fun postDraftOrder(draftOrderPostBody: PostDraftOrderItemModel) : Flow<DraftOrderResponse>
     suspend fun putDraftOrder(id : String,draftOrderPutBody: PutDraftOrderItemModel) : Flow<DraftOrderResponse>
     suspend fun getAllDraftOrders() : Flow<AllDraftOrdersResponse>
     suspend fun getDraftOrderById(id : String) : Flow<DraftOrderResponse>
     suspend fun getCoupons(): Flow<CouponModel>


}
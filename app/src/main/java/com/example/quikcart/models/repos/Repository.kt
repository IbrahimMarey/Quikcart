package com.example.quikcart.models.repos

import com.example.quikcart.models.entities.CategoryItem
import com.example.quikcart.models.entities.AddressesResponse
import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse
import com.example.quikcart.models.entities.Customers
import com.example.quikcart.models.entities.OrdersItem
import com.example.quikcart.models.entities.PostAddressModel
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.SmartCollectionsItem
import com.example.quikcart.models.entities.cart.AllDraftOrdersResponse
import com.example.quikcart.models.entities.cart.DraftOrderResponse
import com.example.quikcart.models.entities.cart.PostDraftOrderItemModel
import com.example.quikcart.models.entities.cart.PutDraftOrderItemModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface Repository {
    fun getBrands():Flow<List<SmartCollectionsItem>>
    fun getProductsByBrandId(id:Long):Flow<List<ProductsItem>>
    fun getProductsBySubCategory(category:String):Flow<List<ProductsItem>>
    suspend fun postCustomer(customerRequest: CustomerRequest): Response<CustomerResponse>
    // shopify
    suspend fun getAllAddressesShopify(customerID:Long,): Flow<AddressesResponse>
    suspend fun postAddressShopify(customerID:Long,address: PostAddressModel)
    suspend fun delAddressShopify(customerID:Long,id:Long)

    fun getProducts(): Flow<List<ProductsItem>>

    fun getCategories():List<CategoryItem>
    fun getCustomer():Flow<List<Customers>>
    fun getCustomerOrders(customerID: Long):Flow<List<OrdersItem>>

    suspend fun postCartItem(
        cartItem: PostDraftOrderItemModel
    ): Flow<PostDraftOrderItemModel>

    suspend fun getCartItems(): Flow<AllDraftOrdersResponse>
    suspend fun delCartItem(id : String)


    suspend fun inertProduct(product: ProductsItem)
    suspend fun deleteProduct(product: ProductsItem)
    fun getAllProducts():Flow<List<ProductsItem>>

    suspend fun postDraftOrder(draftOrderPostBody: PostDraftOrderItemModel) : Flow<DraftOrderResponse>
    suspend fun putDraftOrder(id : String,draftOrderPutBody: PutDraftOrderItemModel) : Flow<DraftOrderResponse>
    suspend fun getAllDraftOrders() : Flow<AllDraftOrdersResponse>
    suspend fun getDraftOrderById(id : String) : Flow<DraftOrderResponse>

}
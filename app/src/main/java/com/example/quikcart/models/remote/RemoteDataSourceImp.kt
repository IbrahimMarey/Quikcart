package com.example.quikcart.models.remote

import android.util.Log
import com.example.quikcart.models.entities.AddressesResponse
import com.example.quikcart.models.entities.CouponModel
import com.example.quikcart.models.entities.CurrencyModel
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
import com.example.quikcart.models.network.ApiService
import com.example.quikcart.models.network.CurrencyService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImp @Inject constructor(private val apiService: ApiService):
    RemoteDataSource {
    override fun getBrands(): Flow<List<SmartCollectionsItem>> = flow {
        val response = apiService.getBrands().smartCollections
        response?.filterNotNull()?.let { emit(it) }
    }

    override fun getProductsByBrandId(id: Long): Flow<List<ProductsItem>> = flow{
        apiService.getProductsByBrandId(id).products.let { emit(it) }
    }

    override fun getProductsBySubCategory(category: String): Flow<List<ProductsItem>> = flow{
        apiService.getProductsBySubCategory(category).products.let { emit(it) }
    }

    override suspend fun postCustomer(customerRequest: CustomerRequest): Response<CustomerResponse> {
        return apiService.postCustomer(customerRequest)
    }


    override suspend fun postAddress(customerID:Long,address: PostAddressModel): Response<FetchAddress> {
        var r =apiService.postAddress(customerID,address)
        return r
    }

    override suspend fun getCustomerAddresses(customerID:Long): Response<AddressesResponse> {
        var r = apiService.getCustomerAddresses(customerID)
        Log.i("TAG", "getCustomerAddresses: ${r.message()}  \n\n ${r.body()}")
        return r
    }

    override suspend fun delCustomerAddress(customerID:Long,id: Long) {
        apiService.delCustomerAddress(customerID,id)
    }
    override fun getProducts(): Flow<List<ProductsItem>> = flow{
       apiService.getProducts().products.let { emit(it) }
    }

    override fun getCustomer(): Flow<List<Customers>> = flow {
        val response = apiService.getCustomers().customers
        emit(response)
    }

    override fun getCustomerOrders(customerID: Long): Flow<List<OrdersItem>> = flow {
        apiService.getCustomerOrders(customerID).orders?.filterNotNull()?.let { emit(it) }
    }

    override suspend fun postCartItem(cartItem: PostDraftOrderItemModel): Flow<PostDraftOrderItemModel> = flow{
        apiService.postCartItem(cartItem).body()?.let {
            Log.i("TAG", "postCartItem: ${it}")
            emit(it)
        }

    }

    override suspend fun getCartItems(): Flow<AllDraftOrdersResponse> = flow{
        apiService.getCartItems().body()?.let {
            Log.i("TAG", "getCartItems: $it")
            emit(it)
        }
    }

    override suspend fun delCartItem(id: String) {
        apiService.delCartItem(id)
    }

    override suspend fun postDraftOrder(draftOrderPostBody: PostDraftOrderItemModel): Flow<DraftOrderResponse> =flow{
        emit(apiService.postDraftOrder(draftOrderPostBody))
    }

    override suspend fun putDraftOrder(
        id: String,
        draftOrderPutBody: PutDraftOrderItemModel
    ): Flow<DraftOrderResponse> = flow{
        emit(apiService.putDraftOrder(id,draftOrderPutBody))
    }

    override suspend fun getAllDraftOrders(): Flow<AllDraftOrdersResponse> = flow{
        emit(apiService.getAllDraftOrders())
    }

    override suspend fun getDraftOrderById(id: String): Flow<DraftOrderResponse> = flow{
        emit(apiService.getDraftOrderById(id))
    }

    override suspend fun getCoupons(): Flow<CouponModel> = flow{
        emit(apiService.getCoupons())
    }



}
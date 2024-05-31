package com.example.quikcart.models.remote

import android.util.Log
import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.models.entities.AddressesResponse
import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse

import com.example.quikcart.models.entities.FetchAddress
import com.example.quikcart.models.entities.PostAddressModel

import com.example.quikcart.models.entities.ProductsItem

import com.example.quikcart.models.entities.SmartCollectionsItem
import com.example.quikcart.models.network.ApiService
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

    override suspend fun postCustomer(customerRequest: CustomerRequest): Response<CustomerResponse> {
        return apiService.postCustomer(customerRequest)
    }


    override suspend fun postAddress(address: PostAddressModel): Response<FetchAddress> {
        var r =apiService.postAddress(address)
        Log.i("TAG", "postAddress: ${r.message()} \n\n ${r.body()}")
        return r
    }

    override suspend fun getCustomerAddresses(): Response<AddressesResponse> {
        var r = apiService.getCustomerAddresses()
        Log.i("TAG", "getCustomerAddresses: ${r.message()}  \n\n ${r.body()}")
        return r
    }

    override suspend fun delCustomerAddress(id: Long) {
        apiService.delCustomerAddress(id)

    override fun getProducts(): Flow<List<ProductsItem>> = flow{
        val response = apiService.getProducts().products
        response?.filterNotNull()?.let { emit(it) }


    }

}
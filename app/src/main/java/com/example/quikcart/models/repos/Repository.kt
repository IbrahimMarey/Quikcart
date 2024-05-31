package com.example.quikcart.models.repos

import com.example.quikcart.models.entities.AddressModel
import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.models.entities.AddressesResponse
import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse

import com.example.quikcart.models.entities.PostAddressModel

import com.example.quikcart.models.entities.ProductsItem

import com.example.quikcart.models.entities.SmartCollectionsItem
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface Repository {
    fun getBrands(): Flow<List<SmartCollectionsItem>>
    fun getProductsByBrandId(id:Long):Flow<List<ProductsItem>>
    suspend fun postCustomer(customerRequest: CustomerRequest): Response<CustomerResponse>
    // shopify
    suspend fun getAllAddressesShopify(): Flow<AddressesResponse>
    suspend fun postAddressShopify(address: PostAddressModel)
    suspend fun delAddressShopify(id:Long)

    fun getProducts(): Flow<List<ProductsItem>>

}
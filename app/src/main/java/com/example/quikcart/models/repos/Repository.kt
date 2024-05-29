package com.example.quikcart.models.repos

import com.example.quikcart.models.entities.AddressModel
import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.SmartCollectionsItem
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface Repository {
    fun getBrands(): Flow<List<SmartCollectionsItem>>
    suspend fun postCustomer(customerRequest: CustomerRequest): Response<CustomerResponse>
    fun getProducts(): Flow<List<ProductsItem>>

    // local
    suspend fun getAllAddresses(): Flow<List<AddressModel>>
    suspend fun insertAddress(addressModel: AddressModel):Long
    suspend fun delAddress(addressModel: AddressModel):Int
}
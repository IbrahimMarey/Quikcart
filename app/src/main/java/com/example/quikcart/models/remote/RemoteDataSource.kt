package com.example.quikcart.models.remote

import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.SmartCollectionsItem
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteDataSource {
     fun getBrands():Flow<List<SmartCollectionsItem>>
     fun getProductsByBrandId(id:Long):Flow<List<ProductsItem>>
     suspend fun postCustomer(customerRequest: CustomerRequest): Response<CustomerResponse>
     fun getProducts():Flow<List<ProductsItem>>
}
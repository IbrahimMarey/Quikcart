package com.example.quikcart.models.remote

import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse
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

    override fun getProductsBySubCategory(category: String): Flow<List<ProductsItem>> = flow{
        apiService.getProductsBySubCategory(category).products.let { emit(it) }
    }

    override suspend fun postCustomer(customerRequest: CustomerRequest): Response<CustomerResponse> {
        return apiService.postCustomer(customerRequest)
    }

    override fun getProducts(): Flow<List<ProductsItem>> = flow{
        val response = apiService.getProducts().products
        response?.filterNotNull()?.let { emit(it) }

    }

}
package com.example.quikcart.models.repos

import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse
import com.example.quikcart.models.entities.SmartCollectionsItem
import com.example.quikcart.models.remote.RemoteDataSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface Repository {
    fun getBrands(): Flow<List<SmartCollectionsItem>>
    suspend fun postCustomer(customerRequest: CustomerRequest): Response<CustomerResponse>
}

class RepositoryImp @Inject constructor(private val brandRemoteRepo: RemoteDataSource):Repository{
    override fun getBrands(): Flow<List<SmartCollectionsItem>> {
        return brandRemoteRepo.getBrands()
    }
    override suspend fun postCustomer(customerRequest: CustomerRequest): Response<CustomerResponse> {
        return brandRemoteRepo.postCustomer(customerRequest)
    }

}
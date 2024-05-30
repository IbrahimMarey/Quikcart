package com.example.quikcart.models.repos

import com.example.quikcart.models.entities.AddressModel
import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse
import com.example.quikcart.models.entities.ProductsItem
import com.example.quikcart.models.entities.SmartCollectionsItem
import com.example.quikcart.models.local.LocalDataSourceInterface
import com.example.quikcart.models.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject


class RepositoryImp @Inject constructor(private val remoteDataSource: RemoteDataSource, private val localDataSourceInterface: LocalDataSourceInterface):Repository{
    override fun getBrands(): Flow<List<SmartCollectionsItem>> {
        return remoteDataSource.getBrands()
    }

    override fun getProductsByBrandId(id: Long): Flow<List<ProductsItem>> {
        return brandRemoteRepo.getProductsByBrandId(id)
    }

    override suspend fun postCustomer(customerRequest: CustomerRequest): Response<CustomerResponse> {
        return remoteDataSource.postCustomer(customerRequest)
    }

    override fun getProducts(): Flow<List<ProductsItem>> {
        return  remoteDataSource.getProducts()
    }

    override suspend fun getAllAddresses(): Flow<List<AddressModel>> = flow<List<AddressModel>>{
        emit(localDataSourceInterface.getAllAddresses())

    }.flowOn(Dispatchers.IO)

    override suspend fun insertAddress(addressModel: AddressModel): Long {
        return localDataSourceInterface.insertAddress(addressModel)
    }

    override suspend fun delAddress(addressModel: AddressModel): Int {
        return localDataSourceInterface.delAddress(addressModel)
    }

}
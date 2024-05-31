package com.example.quikcart.models.repos

import android.util.Log
import com.example.quikcart.models.entities.AddressModel
import com.example.quikcart.models.entities.AddressResponse
import com.example.quikcart.models.entities.AddressesResponse
import com.example.quikcart.models.entities.CustomerRequest
import com.example.quikcart.models.entities.CustomerResponse

import com.example.quikcart.models.entities.PostAddressModel

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
        return remoteDataSource.getProductsByBrandId(id)
    }

    override suspend fun postCustomer(customerRequest: CustomerRequest): Response<CustomerResponse> {
        return remoteDataSource.postCustomer(customerRequest)
    }
    override suspend fun getAllAddressesShopify() : Flow<AddressesResponse> = flow {
        remoteDataSource.getCustomerAddresses().body()?.let {
            emit(it)
            Log.i("TAG", "getAllAddressesShopify: ${it.addresses}")
        }


    override fun getProducts(): Flow<List<ProductsItem>> {
        return  remoteDataSource.getProducts()
    }

    }.flowOn(Dispatchers.IO)
    override suspend fun postAddressShopify(address: PostAddressModel) {
        remoteDataSource.postAddress(address)
    }
    override suspend fun delAddressShopify(id:Long) {
        remoteDataSource.delCustomerAddress(id)
    }

}
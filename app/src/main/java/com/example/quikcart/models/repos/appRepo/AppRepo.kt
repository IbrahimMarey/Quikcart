package com.example.quikcart.models.repos.appRepo

import com.example.quikcart.models.entities.AddressModel
import com.example.quikcart.models.local.LocalDataSource
import com.example.quikcart.models.local.LocalDataSourceInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.Dispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepo @Inject constructor(val localDataSourceInterface: LocalDataSourceInterface) :AppRepoInterface {

    companion object{
        private var appRepo :AppRepo? = null
        fun getInstance(localDataSourceInterface: LocalDataSourceInterface):AppRepo
        {
            return appRepo ?: synchronized(this){
                val instance = AppRepo(localDataSourceInterface)
                appRepo=instance
                instance
            }
        }
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
package com.example.quikcart.models.local

import android.content.Context
import com.example.quikcart.models.entities.AddressModel
import javax.inject.Inject

class LocalDataSource @Inject constructor(appDao: AppDao) :LocalDataSourceInterface {

    private val dao = appDao
    override suspend fun getAllAddresses(): List<AddressModel> {
        return dao.getAllAddresses()
    }

    override suspend fun insertAddress(addressModel: AddressModel): Long {
        return dao.insertAddress(addressModel)
    }

    override suspend fun delAddress(addressModel: AddressModel): Int {
        return dao.delAddress(addressModel)
    }


}
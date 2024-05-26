package com.example.quikcart.models.local

import com.example.quikcart.models.entities.AddressModel
import kotlinx.coroutines.flow.Flow

interface LocalDataSourceInterface
{
    suspend fun getAllAddresses(): List<AddressModel>
    suspend fun insertAddress(addressModel: AddressModel):Long
    suspend fun delAddress(addressModel: AddressModel):Int
}
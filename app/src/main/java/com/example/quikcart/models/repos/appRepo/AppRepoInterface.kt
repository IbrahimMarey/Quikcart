package com.example.quikcart.models.repos.appRepo

import com.example.quikcart.models.entities.AddressModel
import kotlinx.coroutines.flow.Flow

interface AppRepoInterface {
    suspend fun getAllAddresses(): Flow<List<AddressModel>>
    suspend fun insertAddress(addressModel: AddressModel):Long
    suspend fun delAddress(addressModel: AddressModel):Int
}
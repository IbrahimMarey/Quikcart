package com.example.quikcart.models.remote

import com.example.quikcart.models.entities.CurrencyModel
import kotlinx.coroutines.flow.Flow

interface CurrencySourceInterface {
    suspend fun getCurrency(): Flow<CurrencyModel>

}
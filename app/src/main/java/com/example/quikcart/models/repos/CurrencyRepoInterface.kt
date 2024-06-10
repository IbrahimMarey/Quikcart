package com.example.quikcart.models.repos

import com.example.quikcart.models.entities.CurrencyModel
import kotlinx.coroutines.flow.Flow

interface CurrencyRepoInterface {
    suspend fun getCurrency(): Flow<CurrencyModel>

}
package com.example.quikcart.models.remote

import com.example.quikcart.models.entities.CurrencyModel
import com.example.quikcart.models.network.CurrencyService import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CurrencySource(private val currencyService : CurrencyService) :CurrencySourceInterface {
    override suspend fun getCurrency(): Flow<CurrencyModel> =  flow{
        emit(currencyService.getCurrency())
    }
}
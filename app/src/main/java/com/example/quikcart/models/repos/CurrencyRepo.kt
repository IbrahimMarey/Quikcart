package com.example.quikcart.models.repos

import com.example.quikcart.models.entities.CurrencyModel
import com.example.quikcart.models.remote.CurrencySourceInterface
import kotlinx.coroutines.flow.Flow

class CurrencyRepo(private val currencySourceInterface: CurrencySourceInterface) :CurrencyRepoInterface {
    override suspend fun getCurrency(): Flow<CurrencyModel> {
        return currencySourceInterface.getCurrency()
    }

    companion object{
        private var currencyRepoInstance:CurrencyRepo? = null
        fun getInstance(remoteDataSource: CurrencySourceInterface):CurrencyRepo
        {
            return currencyRepoInstance?: synchronized(this)
            {
                var  instance = CurrencyRepo(remoteDataSource)
                currencyRepoInstance=instance
                instance
            }
        }
    }
}
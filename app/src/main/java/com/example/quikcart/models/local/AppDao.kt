package com.example.quikcart.models.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.quikcart.models.entities.AddressModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM addresses")
    suspend fun getAllAddresses():List<AddressModel>

    @Insert
    suspend fun insertAddress(addressModel:AddressModel):Long

    @Delete
    suspend fun delAddress(addressModel: AddressModel):Int
}
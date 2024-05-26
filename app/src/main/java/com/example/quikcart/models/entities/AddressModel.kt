package com.example.quikcart.models.entities

import androidx.room.Entity

@Entity("addresses", primaryKeys = ["lat","lng"])
data class AddressModel(val title:String,val phone:String,val lat:Double,val lng:Double)

package com.example.quikcart.models.entities

import androidx.room.Entity

@Entity("addresses", primaryKeys = ["lat","lng"])
data class AddressModel(val title:String,val phone:String,val lat:Double,val lng:Double)

data class  PostAddressModel(val address : PostAddressBodyModel)

data class PostAddressBodyModel(
    var address1: String,
    var address2: String?,
    var city: String,
    var company: String?,
    var first_name: String,
    var last_name: String,
    var phone: String,
    var province: String,
    var country: String,
    var zip: String,
    var name: String,
    var province_code: String,
    var country_code: String,
    var country_name: String,
)

data class FetchAddress(val customer_address : AddressResponse )

data class AddressesResponse(val addresses : List<AddressResponse>)

package com.example.quikcart.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder

fun getMarkerAddress(context: Context, lat: Double, lon: Double):String{
    var address:MutableList<Address>?
    val geocoder= Geocoder(context)
    address =geocoder.getFromLocation(lat,lon,1)
    return address?.get(0)?.countryName.toString()+", "+address?.get(0)?.adminArea+", "+address?.get(0)?.subAdminArea
}
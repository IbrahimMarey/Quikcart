package com.example.quikcart.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder

fun getMarkerAddress(context: Context, lat: Double, lon: Double):String{
    var address:MutableList<Address>?
    val geocoder= Geocoder(context)
    address =geocoder.getFromLocation(lat,lon,1)
    return address?.get(0)?.countryName.toString()+", "+address?.get(0)?.adminArea.toString()+", "+address?.get(0)?.subAdminArea.toString()
}

fun isLocationInEgypt(latitude: Double, longitude: Double): Boolean {
    val egyptMinLatitude = 22.0
    val egyptMaxLatitude = 31.6
    val egyptMinLongitude = 24.7
    val egyptMaxLongitude = 35.8

    return latitude in egyptMinLatitude..egyptMaxLatitude &&
            longitude in egyptMinLongitude..egyptMaxLongitude
}

fun isEgyptianPhoneNumberValid(phoneNumber: String): Boolean {
    val cleanedPhoneNumber = phoneNumber.replace(Regex("[^\\d]"), "")

    if (!cleanedPhoneNumber.startsWith("01")) {
        return false
    }

    if (cleanedPhoneNumber.length !in 10..11) {
        return false
    }
    val validMobilePrefixes = listOf("010", "011", "012", "015")
    return validMobilePrefixes.any { cleanedPhoneNumber.startsWith(it) }
}
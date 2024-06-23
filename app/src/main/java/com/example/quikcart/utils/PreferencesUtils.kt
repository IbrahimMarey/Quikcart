package com.example.quikcart.utils

import android.content.Context
import android.content.SharedPreferences

private const val SETTINGS_SHARED_PREFERENCES = "SETTINGS_SHARED_PREFERENCES"
private const val CURRENCY_PREFERENCE = "SETTING_CURRENCY"
private const val USER_ID_PREFERENCE = "USER_ID"
private const val CART_ID_PREFERENCE = "CART_ID"
private const val CUSTOMER_ID = "CUSTOMER_ID"
private const val CUSTOMER_NAME = "CUSTOMER_NAME"
private const val CUSTOMER_EMAIL = "CUSTOMER_EMAIL"
private const val USD_RATE = "USD_RATE"
private const val FAVOURITE_ID = "FAVOURITE_LIST"
class PreferencesUtils internal constructor(context: Context)
{
    private var sharedPreferences : SharedPreferences = context.getSharedPreferences(
        SETTINGS_SHARED_PREFERENCES,Context.MODE_PRIVATE)

    private val editor : SharedPreferences.Editor = sharedPreferences.edit()

    fun setCurrencyType(type:String)
    {
        editor.putString(CURRENCY_PREFERENCE , type).apply()
    }

    fun getCurrencyType(): String?
    {
        return sharedPreferences.getString(CURRENCY_PREFERENCE,"EGP")
    }

    fun setUserID(id:String)
    {
        editor.putString(USER_ID_PREFERENCE , id).apply()
    }

    fun getUserId(): String?
    {
        return sharedPreferences.getString(USER_ID_PREFERENCE,"0")
    }
    fun setCartId(id:Long){
        editor.putLong(CART_ID_PREFERENCE,id).apply()
    }
    fun getCartId():Long{
        return sharedPreferences.getLong(CART_ID_PREFERENCE,0)
    }
    fun setFavouriteId(id:Long){
        editor.putLong(FAVOURITE_ID,id).apply()
    }
    fun getFavouriteId():Long{
        return sharedPreferences.getLong(FAVOURITE_ID,0)
    }
    fun setCustomerId(id:Long){
        editor.putLong(CUSTOMER_ID,id).apply()
    }
    fun getCustomerId():Long{
        return sharedPreferences.getLong(CUSTOMER_ID,0)
    }


    fun saveCustomerName(name:String){
        editor.putString(CUSTOMER_NAME,name).apply()
    }
    fun getCustomerName(): String? {
        return sharedPreferences.getString(CUSTOMER_NAME,"")
    }

    fun setUSDRate(id:Float){
        editor.putFloat(USD_RATE,id).apply()
    }
    fun getUSDRate():Float{
        return sharedPreferences.getFloat(USD_RATE, 0.0F)
    }

    fun saveCustomerEmail(email:String){
        editor.putString(CUSTOMER_EMAIL,email).apply()
    }
    fun getCustomerEmail(): String? {
        return sharedPreferences.getString(CUSTOMER_EMAIL,"")
    }
    companion object{
        const val CURRENCY_USD = "USD"
        const val CURRENCY_EGP = "EGP"
        var isPayWithPayPal = false
        private var instance  : PreferencesUtils? = null
        fun getInstance(context:Context):PreferencesUtils{
            return instance?: synchronized(this){
                val pref = PreferencesUtils(context)
                instance=pref
                pref
            }
        }

    }
}
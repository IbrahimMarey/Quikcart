package com.example.quikcart.utils

import android.content.Context
import android.content.SharedPreferences

private const val SETTINGS_SHARED_PREFERENCES = "SETTINGS_SHARED_PREFERENCES"
private const val CURRENCY_PREFERENCE = "SETTING_CURRENCY"
private const val USER_ID_PREFERENCE = "USER_ID"
class PreferencesUtils private constructor(context: Context)
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
    fun setCustomerId(id:Long){
        editor.putLong("id",id).apply()
    }
    fun getCustomerId():Long{
        return sharedPreferences.getLong("id",0)
    }
    companion object{
        const val CURRENCY_USD = "USD"
        const val CURRENCY_EGP = "EGP"

        private var instance  : PreferencesUtils? = null
        fun getInstance(context:Context):PreferencesUtils{
            return instance?: synchronized(this){
                var pref = PreferencesUtils(context)
                instance=pref
                pref
            }
        }

    }
}
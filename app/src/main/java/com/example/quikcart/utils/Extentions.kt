package com.example.quikcart.utils

import android.content.Context
import android.widget.TextView

fun TextView.setPrice(price:Float,ctx: Context){
    var txtPrice = price
    if (!PreferencesUtils.getInstance(ctx).getCurrencyType().equals(PreferencesUtils.CURRENCY_EGP))
    {
        txtPrice *= 0.021f
    }
    text = buildString {
        append(txtPrice)
        append(" ${PreferencesUtils.getInstance(ctx).getCurrencyType()}")
    }
}
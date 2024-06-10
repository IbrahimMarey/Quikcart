package com.example.quikcart.utils

import android.content.Context
import android.widget.TextView

fun TextView.setPrice(price: Float, ctx: Context) {
    val pref = PreferencesUtils.getInstance(ctx)
    var txtPrice = price
    if (!pref.getCurrencyType().equals(PreferencesUtils.CURRENCY_EGP)) {
        txtPrice *= pref.getUSDRate() // 0.021f
        txtPrice = "%.2f".format(txtPrice).toFloat()
    }
    text = buildString {
        append("%.2f".format(txtPrice))
        append(" ${pref.getCurrencyType()}")
    }
}
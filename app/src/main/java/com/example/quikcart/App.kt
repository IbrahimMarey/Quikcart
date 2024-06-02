package com.example.quikcart

import android.app.Application
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application()
{
    override fun onCreate() {
        super.onCreate()
        val config = CheckoutConfig(
            application = this,
            clientId = BuildConfig.PAYPAL_CLIENT_ID,
            environment = Environment.SANDBOX,
            returnUrl = BuildConfig.PayPalUrl,
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true,
                showWebCheckout = false
            )
        )
        PayPalCheckout.setConfig(config)
    }
}
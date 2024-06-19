package com.example.quikcart.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NetworkChangeReceiver(private val onNetworkChange:(Boolean)->Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val isConnected=NetworkUtil.isNetworkAvailable(context)
        onNetworkChange(isConnected)
    }
}
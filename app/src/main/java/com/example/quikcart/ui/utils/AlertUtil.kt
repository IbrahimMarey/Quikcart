package com.example.quikcart.ui.utils

import android.content.Context
import android.widget.Toast

object AlertUtil {
    fun showToast(ctx:Context,msg:String){
        Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show()
    }
}
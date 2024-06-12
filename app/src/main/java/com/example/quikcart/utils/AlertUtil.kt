package com.example.quikcart.utils

import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast

object AlertUtil {
    private var progressDialog:ProgressDialog?=null
    fun showToast(ctx:Context,msg:String){
        Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show()
    }

    fun showProgressDialog(ctx:Context){
        progressDialog=ProgressDialog(ctx).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
        progressDialog?.show()
    }
    fun hideProgressDialog(){
        progressDialog?.dismiss()
    }
}
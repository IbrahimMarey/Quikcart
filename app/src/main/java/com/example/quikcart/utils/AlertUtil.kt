package com.example.quikcart.utils

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast


object AlertUtil {
    private var progressDialog:ProgressDialog?=null
    private var alertDialog: AlertDialog? = null
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

    fun showToastMessage(message: String?, context: Context?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showCustomAlertDialog(
        context: Context,
        title:String?=null,
        message: String?=null,
        positiveText: String?=null,
        positiveClickListener: DialogInterface.OnClickListener?=null,
        negText:String?=null,
        negClickListener: DialogInterface.OnClickListener?=null,
    ) {
        alertDialog = AlertDialog.Builder(context)
        .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText, positiveClickListener)
            .setNegativeButton(negText,negClickListener)
            .show()
    }

    fun dismissAlertDialog(){
        alertDialog?.dismiss()
    }
}
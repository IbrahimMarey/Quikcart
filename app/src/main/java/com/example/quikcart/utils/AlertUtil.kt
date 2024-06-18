package com.example.quikcart.utils

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
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

    fun showToastMessage(message: String?, context: Context?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showCustomAlertDialog(
        context: Context,
        message: String?=null,
        positiveText: String?=null,
        positiveClickListener: DialogInterface.OnClickListener?=null
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("")
            .setMessage(message)
            .setPositiveButton(positiveText, positiveClickListener)
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
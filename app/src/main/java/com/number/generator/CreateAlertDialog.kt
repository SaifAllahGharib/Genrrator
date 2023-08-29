package com.number.generator

import android.app.AlertDialog
import android.content.Context
import android.view.View

class CreateAlertDialog(
    context: Context,
    var view: View,
    them: Int,
    setCancelable: Boolean = true
) {
    private var alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context, them)
    private var alertDialog: AlertDialog

    init {
        alertDialogBuilder.setView(view)
        alertDialogBuilder.setCancelable(setCancelable)
        alertDialog = alertDialogBuilder.create()
    }

    fun showAlertDialog() {
        alertDialog.show()
    }

    fun alertDialogDismiss() {
        alertDialog.dismiss()
    }
}


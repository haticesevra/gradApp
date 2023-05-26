package com.example.gradapp.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

object Utility {
    //  val apiUrl: String = "http://localhost:5000"

    //val apiUrl: String = "http://10.0.2.2:5000"
    //val apiUrl: String = "http://10.22.122.49:5000"
    val apiUrl: String = "http://192.168.1.35:5000"

    fun showAlert( context: Context,
                   title: String="",
                    message: String = "",
                   onYes: Runnable? = null,
                   onNo: Runnable? = null

                   ){
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(android.R.string.yes){
            dialog, which ->
            onYes?.run()
        }
        alertDialogBuilder.setNegativeButton(android.R.string.no){
                dialog, which ->
            onNo?.run()
        }

        alertDialogBuilder.show()
    }

}
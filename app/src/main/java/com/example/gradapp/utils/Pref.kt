package com.example.gradapp.utils

import android.content.Context
import android.content.SharedPreferences

class Pref {
    private val fileName: String = "MY_PREF"

    fun removeAccessToken(context: Context){
        val preference: SharedPreferences =
            context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        preference.edit().remove("token").apply()
    }

    fun getAccessToken(context: Context): String{
        val preference: SharedPreferences =
            context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        return preference.getString("token","" ).toString()
    }

    fun setAccessToken(context: Context, token:String){
        val preference: SharedPreferences =
            context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        preference.edit().putString("token", token).apply()
    }

}
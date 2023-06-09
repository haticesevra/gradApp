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

    fun getUserEmail(context: Context): String {
        val preference: SharedPreferences =
            context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        return preference.getString("email", "").toString()
    }

    fun setUserEmail(context: Context, email: String) {
        val preference: SharedPreferences =
            context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        preference.edit().putString("email", email).apply()
    }

    fun getUserName(context: Context): String {
        val preference: SharedPreferences =
            context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        return preference.getString("name", "").toString()
    }

    fun setUserName(context: Context, name: String) {
        val preference: SharedPreferences =
            context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        preference.edit().putString("name", name).apply()
    }


    fun getCorrName(context: Context): String {
        val preference: SharedPreferences =
            context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        return preference.getString("corrName", "").toString()
    }

    fun setCorrName(context: Context, corrName: String) {
        val preference: SharedPreferences =
            context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        preference.edit().putString("corrName", corrName).apply()
    }

    fun getCorrParameter(context: Context): String {
        val preference: SharedPreferences =
            context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        return preference.getString("corrParam", "").toString()
    }

    fun setCorrParameter(context: Context, corrParam: String) {
        val preference: SharedPreferences =
            context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        preference.edit().putString("corrParam", corrParam).apply()
    }



    fun removeCorrName(context: Context){
        val preference: SharedPreferences =
            context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        preference.edit().remove("corrName").apply()
    }

    fun removeCorrParameter(context: Context){
        val preference: SharedPreferences =
            context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
        preference.edit().remove("corrParam").apply()
    }
}

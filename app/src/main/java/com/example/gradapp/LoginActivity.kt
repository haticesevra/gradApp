package com.example.gradapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gradapp.models.LoginModel
import com.example.gradapp.utils.Pref
import com.example.gradapp.utils.Utility
import com.google.gson.Gson
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var loginBtn : Button
    lateinit var gotoRegister : TextView
    lateinit var forgotPwd : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        title = "Login"

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginBtn = findViewById(R.id.loginBtn)
        gotoRegister = findViewById(R.id.gotoRegister)
        forgotPwd = findViewById(R.id.forgotPwd)


        gotoRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        forgotPwd.setOnClickListener{
            startActivity(Intent(this, SendMailOtpCode::class.java))
            finish()
        }

        loginBtn.setOnClickListener{
            val queue = Volley.newRequestQueue(applicationContext)
            val url: String = Utility.apiUrl + "/login"
            val jsonObject = JSONObject().apply {
                put("email", email.text.trim().toString())
                put("password", password.text.toString())
            }

            val stringRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    Log.i("mylog1", response.toString())
                    //val generalResponse: GeneralResponse =  Gson().fromJson(response.toString(), GeneralResponse::class.java)
                    val loginModel: LoginModel =  Gson().fromJson(response.toString(), LoginModel::class.java)
                   if(loginModel.status == "OK"){
                       val preference: Pref = Pref()
                       preference.setAccessToken(this, loginModel.token )
                       startActivity(Intent(this, HomeActivity::class.java))
                       finish()
                   }
                    else{
                       Utility.showAlert(this, "Error")

                   }

                },
                { error ->

                    Log.i("mylog2", error.message.toString())
                }
            )

            queue.add(stringRequest)
        }

    }
}
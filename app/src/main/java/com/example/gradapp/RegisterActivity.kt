package com.example.gradapp

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gradapp.models.GeneralResponse
import com.example.gradapp.utils.Utility
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.Charset

class RegisterActivity : AppCompatActivity() {

    lateinit var name : EditText
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var registerBtn : Button
    lateinit var gotoLogin : TextView
    //lateinit var showHideBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        title = "Register"

        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        registerBtn = findViewById(R.id.registerBtn)
        gotoLogin = findViewById(R.id.gotoLogin)
        //showHideBtn = findViewById(R.id.showHideBtn)

        gotoLogin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        registerBtn.setOnClickListener{
            val queue = Volley.newRequestQueue(applicationContext)
            val url: String = Utility.apiUrl + "/register"
            val jsonObject = JSONObject().apply {
                put("name", name.text.trim().toString())
                put("email", email.text.trim().toString())
                put("password", password.text.toString())
            }

            val stringRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    Log.i("mylog1", response.toString())
                   val generalResponse: GeneralResponse =  Gson().fromJson(response.toString(), GeneralResponse::class.java)
                    Utility.showAlert(this, "Register", generalResponse.message)
                    if(generalResponse.status == "OK"){
                        startActivity(Intent(this, LoginActivity::class.java))
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


       /* showHideBtn.setOnClickListener {
            if(showHideBtn.text.toString().equals("Show")){
                password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                showHideBtn.text = "Hide"
            } else{
                password.transformationMethod = PasswordTransformationMethod.getInstance()
                showHideBtn.text = "Show"
            }
        }*/
    }


}
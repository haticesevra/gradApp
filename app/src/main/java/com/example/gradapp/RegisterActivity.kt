package com.example.gradapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gradapp.utils.Utility
import java.nio.charset.Charset

class RegisterActivity : AppCompatActivity() {

    lateinit var name : EditText
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var registerBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        registerBtn = findViewById(R.id.registerBtn)

        registerBtn.setOnClickListener{
            val queue = Volley.newRequestQueue(applicationContext)
            val url: String = Utility.apiUrl + "/register"

            val requestBody: String = "name=" + name.text +
                                    "email=" + email.text +
                                    "&password" + password.text
            val stringRequest: StringRequest = object : StringRequest(Method.POST, url,
                                    Response.Listener {response ->
                                        Log.i("mylog", response)

                                    },
                                    Response.ErrorListener { error ->
                                        Log.i("mylog", error.message.toString())
                                    })
            {
                override fun getBody(): ByteArray {
                    return requestBody.toByteArray(Charset.defaultCharset())
                }
            }

            queue.add(stringRequest)
        }

    }


}
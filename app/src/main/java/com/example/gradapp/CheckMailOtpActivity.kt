package com.example.gradapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.gradapp.models.LoginModel
import com.example.gradapp.utils.Utility
import com.google.gson.Gson
import org.json.JSONObject

class CheckMailOtpActivity : AppCompatActivity() {

    lateinit var digit1 : EditText
    lateinit var digit2 : EditText
    lateinit var digit3 : EditText
    lateinit var digit4 : EditText
    lateinit var digit5 : EditText
    lateinit var digit6 : EditText
    lateinit var verifyButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_mail_otp)

        digit1 = findViewById(R.id.digit1)
        digit2 = findViewById(R.id.digit2)
        digit3 = findViewById(R.id.digit3)
        digit4 = findViewById(R.id.digit4)
        digit5 = findViewById(R.id.digit5)
        digit6 = findViewById(R.id.digit6)
        verifyButton = findViewById(R.id.verifyButton)

        val otpCode = StringBuilder()
        otpCode.append(digit1.text)
        otpCode.append(digit2.text)
        otpCode.append(digit3.text)
        otpCode.append(digit4.text)
        otpCode.append(digit5.text)
        otpCode.append(digit6.text)

        val otpString = otpCode.toString()

        println("OTP:  " + otpString)

        verifyButton.setOnClickListener{
            val queue = Volley.newRequestQueue(applicationContext)
            val url: String = Utility.apiUrl + "/verifyMailOtp"
            val jsonObject = JSONObject().apply {
                put("email", "haticesevragenc@gmail.com")  //değiştirrr
                put("otp", otpString)
            }

            val stringRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    Log.i("mylog1", response.toString())
                    val loginModel: LoginModel =  Gson().fromJson(response.toString(), LoginModel::class.java)
                    if(loginModel.status == "OK"){
                        startActivity(Intent(this, ChangePasswordActivity::class.java))
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
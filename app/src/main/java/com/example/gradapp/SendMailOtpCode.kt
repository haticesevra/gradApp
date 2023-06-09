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
import com.example.gradapp.models.GeneralResponse
import com.example.gradapp.utils.Utility
import com.google.gson.Gson
import org.json.JSONObject

class SendMailOtpCode : AppCompatActivity() {

    lateinit var email : EditText
    private lateinit var sendOtpButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_mail_otp_code)


        title = "Send Mail OTP Code"

        email = findViewById(R.id.email)
        sendOtpButton = findViewById(R.id.sendOtpButton)



        sendOtpButton.setOnClickListener {
            val queue = Volley.newRequestQueue(applicationContext)
            val url: String = Utility.apiUrl + "/sendMailOtp"
            val jsonObject = JSONObject().apply {
                put("email", email.text.toString())
            }

            val stringRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    Log.i("mylog1", response.toString())
                    val generalResponse: GeneralResponse =  Gson().fromJson(response.toString(), GeneralResponse::class.java)
                    if (generalResponse.status == "OK") {
                        startActivity(Intent(this, CheckMailOtpActivity::class.java))
                        finish()
                    } else {
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
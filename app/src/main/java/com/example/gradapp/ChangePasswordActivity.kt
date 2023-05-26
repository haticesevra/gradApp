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

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var password : EditText
    private lateinit var passwordAgain : EditText
    private lateinit var changePwdButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        password = findViewById(R.id.password)
        passwordAgain = findViewById(R.id.passwordAgain)
        changePwdButton = findViewById(R.id.changePwdButton)



        changePwdButton.setOnClickListener {
            val enteredPassword = password.text.trim().toString()
            val enteredPasswordAgain = passwordAgain.text.trim().toString()

            if (enteredPassword == enteredPasswordAgain) {
                val queue = Volley.newRequestQueue(applicationContext)
                val url: String = Utility.apiUrl + "/changePassword"
                val jsonObject = JSONObject().apply {
                    put("email", "haticesevragenc@gmail.com") // Replace with the appropriate email
                    put("newPassword", enteredPassword)
                }

                val stringRequest = JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    { response ->
                        Log.i("mylog1", response.toString())
                        val loginModel: LoginModel = Gson().fromJson(response.toString(), LoginModel::class.java)
                        if (loginModel.status == "OK") {
                            startActivity(Intent(this, LoginActivity::class.java))
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
            } else {
                Utility.showAlert(this, "Passwords do not match")
            }
        }

    }

}
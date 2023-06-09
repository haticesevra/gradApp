package com.example.gradapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gradapp.models.CorrelationDataModel
import com.example.gradapp.models.GeneralResponse
import com.example.gradapp.models.LoginModel
import com.example.gradapp.utils.Pref
import com.example.gradapp.utils.Utility
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class CheckMailOtpCorrActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var digit1 : EditText
    lateinit var digit2 : EditText
    lateinit var digit3 : EditText
    lateinit var digit4 : EditText
    lateinit var digit5 : EditText
    lateinit var digit6 : EditText
    lateinit var verifyButton : Button
    lateinit var correlationData: CorrelationDataModel
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var pref: Pref
    lateinit var parameter: String
    lateinit var correlationBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_mail_otp_corr)

        drawerLayout = findViewById(R.id.drawerLayout)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout,
                R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView = findViewById(R.id.navView)
        navView.setNavigationItemSelectedListener(this)
        // Set up header fields
        val headerView = navView.getHeaderView(0)
        val navUsername = headerView.findViewById<TextView>(R.id.name)
        val navUserEmail = headerView.findViewById<TextView>(R.id.email)

        val pref = Pref()
        navUsername.text = pref.getUserName(this)
        navUserEmail.text = pref.getUserEmail(this)

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
        val preference: Pref = Pref()
        val userEmail = preference.getUserEmail(applicationContext)
        val gson = Gson()

        verifyButton.setOnClickListener{
            val queue = Volley.newRequestQueue(applicationContext)
            val url: String = Utility.apiUrl + "/verifyCorrMailOtp"
            val jsonObject = JSONObject().apply {
                put("email", userEmail)
                put("otp", otpString)
            }

            val stringRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    Log.i("mylog1", response.toString())
                    val genResp: GeneralResponse =  Gson().fromJson(response.toString(), GeneralResponse::class.java)

                    if(genResp.status == "OK"){


                        val urlGetCorr: String = Utility.apiUrl + "/correlation?" + preference.getCorrParameter(applicationContext)
                        val stringRequestGetCorr = StringRequest(
                            Request.Method.GET, urlGetCorr,
                            { response ->
                                Log.i("mylog1", response.toString())
                                // val generalResponse: GeneralResponse = Gson().fromJson(response.toString(), GeneralResponse::class.java)
                                correlationData = Gson().fromJson(response.toString(), CorrelationDataModel::class.java)
                                if (correlationData.status == "OK") {

                                    val listDataAsString = correlationData.data.map { gson.toJson(it) }
                                    println("CORRDATA: " + correlationData.data)
                                    println("CORRDATA2: " + listDataAsString)
                                    setContentView(R.layout.activity_home)




                                    val jsonObject2 = JSONObject().apply {
                                        put("name", preference.getCorrName(applicationContext))
                                        put("corrParameter", preference.getCorrParameter(applicationContext))
                                        put("filteredData", listDataAsString)
                                    }


                                    val url2: String = Utility.apiUrl + "/sendCorrData"

                                    val stringRequest2 = JsonObjectRequest(
                                        Request.Method.POST, url2, jsonObject2,
                                        { response ->
                                            // Handle the response here

                                            Log.i("mylog1", response.toString())
                                            startActivity(Intent(this, CorrelationListActivity::class.java))
                                            finish()
                                        },
                                        { error ->
                                            // Handle the error here
                                            Log.i("mylog2", error.message.toString())
                                        }
                                    )

                                    queue.add(stringRequest2)



                                }

                            },
                            { error ->
                                Log.i("mylog2", error.message.toString())
                            }
                        )

                        queue.add(stringRequestGetCorr)
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


    private fun doLogout(){
        pref.removeAccessToken(this)
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            doLogout()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
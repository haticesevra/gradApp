package com.example.gradapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gradapp.models.CorrelationDataModel
import com.example.gradapp.utils.Pref
import com.example.gradapp.utils.Utility
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import org.json.JSONObject

class CorrelationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    lateinit var spinner1: Spinner
    lateinit var spinner2: Spinner
    lateinit var addBtn : Button
    lateinit var correlationData: CorrelationDataModel
    lateinit var corrList: TextView
    lateinit var createCorrBtn: Button
    lateinit var corrName: EditText

    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var pref: Pref
    lateinit var parameter: String

    // mapping for the first and second spinner
    val dropdownValues = hashMapOf(
        "Category" to listOf("Application", "Database", "J2EE", "WebService"),
        "Subcategory" to listOf("Apache", "Cassandra", "Exchange", "Firebase", "Hive", "IBMDB2", "IIS", "Java", "MongoDB", "MySql", "NGINX", "Oracle", "PostgreSQL", "SQLServer", "TOMCAT", "WAS"),
        "Status" to listOf("Close", "ARROW", "open", "PROBLEM"),
        "Severity" to listOf("Critical", "Major", "Minor", "Normal", "Unknown", "Warning"),
        "Source" to listOf("AlienVault", "ArcSight", "CryptoLog", "FortiSIEM", "IBM QRadar", "Insight", "LogPoint", "LogRhythm", "Ops Bridge", "PRTG", "SiteScope", "Solarwinds", "Splunk", "Zabbix"),
        "EtiValue" to listOf("Error", "Good", "High", "Low", "Warning"),
        "EtiType" to listOf("CONNECTION", "CPU USAGE", "HALTED", "LINK DOWN", "LOW DISK", "NEW LOGIN", "POWER ERROR", "SYSTEM OVERLOADED", "TEMPATURE")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_correlation)
        parameter=""

        spinner1 = findViewById(R.id.spinner1)
        spinner2 = findViewById(R.id.spinner2)
        addBtn = findViewById(R.id.addBtn)
        createCorrBtn = findViewById(R.id.createCorrBtn)
        corrList = findViewById(R.id.corrList)
        corrName = findViewById(R.id.corrName)

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


        val spinner1Adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dropdownValues.keys.toList())
        spinner1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = spinner1Adapter
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedValue = parent.getItemAtPosition(position).toString()
                val spinner2Values = dropdownValues[selectedValue]
                if (spinner2Values != null) {
                    val spinner2Adapter = ArrayAdapter(this@CorrelationActivity, android.R.layout.simple_spinner_item, spinner2Values)
                    spinner2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner2.adapter = spinner2Adapter
                }


       val queue = Volley.newRequestQueue(applicationContext)

       addBtn.setOnClickListener{

            // Check if parameter is empty. If not, prepend an "&".
            val newParameter = if (parameter.isEmpty()) {
                spinner1.getSelectedItem().toString()  + "=" + spinner2.getSelectedItem().toString()
            } else {
                "&" + spinner1.getSelectedItem().toString()  + "=" + spinner2.getSelectedItem().toString()
            }

            // Add the new parameter to the existing ones.

           parameter += newParameter

           corrList.text = parameter.replace("&", "\n")
        }


        createCorrBtn.setOnClickListener{
            val corrNameStr = corrName.text.toString()

            // Check if corrName is empty
            if (corrNameStr.trim().isEmpty()) {
                // Show an error message to the user and return early
                Toast.makeText(applicationContext, "Please enter a correlation name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val preference: Pref = Pref()
            val userEmail = preference.getUserEmail(applicationContext)
            val jsonObject = JSONObject().apply {
                put("email", userEmail)
                put("name", corrName.text.toString())
            }
            val urlOtp: String = Utility.apiUrl + "/sendCorrMailOtp"

            val stringRequestOtp = JsonObjectRequest(
                Request.Method.POST, urlOtp, jsonObject,
                { response ->
                    // Handle the successful response
                    Log.i("mylog1", response.toString())
                    preference.setCorrName(applicationContext, corrName.text.toString())
                    preference.setCorrParameter(applicationContext, parameter)


                    startActivity(Intent(applicationContext, CheckMailOtpCorrActivity::class.java))
                    finish()
                    // Process the response data here
                },
                { error ->
                    // Handle the error response
                    Log.i("mylog2", error.message.toString())
                    // Display an error message or perform error handling here
                }
            )

            queue.add(stringRequestOtp)



        }



            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // When no item is selected
            }
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
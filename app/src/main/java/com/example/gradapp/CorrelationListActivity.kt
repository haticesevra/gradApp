package com.example.gradapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gradapp.models.CorrListDataModel
import com.example.gradapp.utils.Utility
import com.google.gson.Gson
import com.android.volley.Request
import com.example.gradapp.utils.Pref
import com.google.android.material.navigation.NavigationView

class CorrelationListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    lateinit var corrList: RecyclerView
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var pref: Pref
    lateinit var parameter: String
    lateinit var correlationBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_correlation_list)
        val preference: Pref = Pref()

        navView = findViewById(R.id.navView)
        navView.setNavigationItemSelectedListener(this)
        // Set up header fields
        val headerView = navView.getHeaderView(0)
        val navUsername = headerView.findViewById<TextView>(R.id.name)
        val navUserEmail = headerView.findViewById<TextView>(R.id.email)

        val pref = Pref()
        navUsername.text = pref.getUserName(this)
        navUserEmail.text = pref.getUserEmail(this)


        preference.removeCorrParameter(applicationContext)
        preference.removeCorrName(applicationContext)

        corrList = findViewById(R.id.corrList)
        corrList.layoutManager = LinearLayoutManager(this)


        drawerLayout = findViewById(R.id.drawerLayout)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout,
                R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        correlationBtn = findViewById(R.id.correlationBtn)


        val queue = Volley.newRequestQueue(applicationContext)

        val url: String = Utility.apiUrl + "/getCorrList"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.i("mylog1", response.toString())
                val corrListData = Gson().fromJson(response.toString(), CorrListDataModel::class.java)
                if (corrListData.status == "OK") {


                    corrList.adapter = CorrelationAdapter(corrListData.data)
                } else {
                    Utility.showAlert(this, "Error")
                }
            },
            { error ->
                Log.i("mylog2", error.message.toString())
            }
        )

        queue.add(stringRequest)

        correlationBtn.setOnClickListener{
            startActivity(Intent(this, CorrelationActivity::class.java))
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
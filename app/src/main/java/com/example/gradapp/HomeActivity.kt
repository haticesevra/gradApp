 package com.example.gradapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gradapp.utils.Pref
import com.example.gradapp.utils.Utility
import com.google.android.material.navigation.NavigationView

 class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var navView: NavigationView
    lateinit var pref: Pref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        pref = Pref()
        drawerLayout = findViewById(R.id.drawerLayout)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout,
                R.string.nav_open, R.string.nav_close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView = findViewById(R.id.navView)
        navView.setNavigationItemSelectedListener(this)



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
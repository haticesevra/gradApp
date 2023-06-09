package com.example.gradapp

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gradapp.models.CorrelationDataModel
import com.example.gradapp.utils.Pref
import com.example.gradapp.utils.Utility
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson

class SummaryActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var severityBtn: Button
    lateinit var etiValueBtn: Button
    lateinit var etiTypeBtn: Button
    lateinit var categoryBtn: Button
    lateinit var subcategoryBtn: Button
    lateinit var sumDetail: TextView
    lateinit var correlationData: CorrelationDataModel
    lateinit var navView: NavigationView
    lateinit var pref: Pref
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        pref = Pref()

        severityBtn = findViewById(R.id.severityBtn)
        etiTypeBtn = findViewById(R.id.etiTypeBtn)
        etiValueBtn = findViewById(R.id.etiValueBtn)
        categoryBtn = findViewById(R.id.categoryBtn)
        subcategoryBtn = findViewById(R.id.subcategoryBtn)
        sumDetail = findViewById(R.id.sumDetail)
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


        val queue = Volley.newRequestQueue(applicationContext)


        val url: String = Utility.apiUrl + "/correlation"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.i("mylog1", response.toString())
                correlationData = Gson().fromJson(response.toString(), CorrelationDataModel::class.java)
                if (correlationData.status == "OK")
                    println("CORRDATA: " + correlationData.data)
                else
                    Utility.showAlert(this, "Error")
            },
            { error ->
                Log.i("mylog2", error.message.toString())
            }
        )

        queue.add(stringRequest)

        severityBtn.setOnClickListener{
            resetButtonColors()
            severityBtn.setBackgroundColor(resources.getColor(R.color.pink1))

            val severitySummary = correlationData.data.groupingBy { it.Severity }.eachCount()
            val total = correlationData.data.size.toDouble()

            val severitySummaryPercentages = severitySummary.mapValues { (it.value / total) * 100 }
            val spannableStringBuilder = SpannableStringBuilder() // Changed this line
            severitySummaryPercentages.forEach { (key, value) ->
                val spannableString = SpannableString("$key\t-> ${"%.2f".format(value)}%\n")
                spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, key.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.pink1)), 0, key.length +3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableStringBuilder.append(spannableString)
            }

            sumDetail.text = spannableStringBuilder // Changed this line
        }


        etiValueBtn.setOnClickListener{
            resetButtonColors()
            etiValueBtn.setBackgroundColor(resources.getColor(R.color.pink1))

            val etiValueSummary = correlationData.data.groupingBy { it.EtiValue }.eachCount()
            val total = correlationData.data.size.toDouble()

            val etiValueSummaryPercentages = etiValueSummary.mapValues { (it.value / total) * 100 }
            val spannableStringBuilder = SpannableStringBuilder() // Changed this line
            etiValueSummaryPercentages.forEach { (key, value) ->
                val spannableString = SpannableString("$key\t-> ${"%.2f".format(value)}%\n")
                spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, key.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.pink1)), 0, key.length +3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableStringBuilder.append(spannableString)
            }
            sumDetail.text = spannableStringBuilder // Changed this line
        }

        etiTypeBtn.setOnClickListener{
            resetButtonColors()
            etiTypeBtn.setBackgroundColor(resources.getColor(R.color.pink1))


            val etiTypeSummary = correlationData.data.groupingBy { it.EtiType }.eachCount()
            val total = correlationData.data.size.toDouble()

            val etiTypeSummaryPercentages = etiTypeSummary.mapValues { (it.value / total) * 100 }
            val spannableStringBuilder = SpannableStringBuilder() // Changed this line
            etiTypeSummaryPercentages.forEach { (key, value) ->
                val spannableString = SpannableString("$key\t-> ${"%.2f".format(value)}%\n")
                spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, key.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.pink1)), 0, key.length +3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableStringBuilder.append(spannableString)
            }
            sumDetail.text = spannableStringBuilder // Changed this line
        }

        categoryBtn.setOnClickListener{
            resetButtonColors()
            categoryBtn.setBackgroundColor(resources.getColor(R.color.pink1))

            val categorySummary = correlationData.data.groupingBy { it.EtiType }.eachCount()
            val total = correlationData.data.size.toDouble()

            val categoryPercentages = categorySummary.mapValues { (it.value / total) * 100 }
            val spannableStringBuilder = SpannableStringBuilder() // Changed this line
            categoryPercentages.forEach { (key, value) ->
                val spannableString = SpannableString("$key\t-> ${"%.2f".format(value)}%\n")
                spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, key.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.pink1)), 0, key.length +3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableStringBuilder.append(spannableString)
            }
            sumDetail.text = spannableStringBuilder // Changed this line
        }

        subcategoryBtn.setOnClickListener{
            resetButtonColors()
            subcategoryBtn.setBackgroundColor(resources.getColor(R.color.pink1))

            val subcategorySummary = correlationData.data.groupingBy { it.Subcategory }.eachCount()
            val total = correlationData.data.size.toDouble()

            val subcategorySummaryPercentages = subcategorySummary.mapValues { (it.value / total) * 100 }
            val spannableStringBuilder = SpannableStringBuilder() // Changed this line
            subcategorySummaryPercentages.forEach { (key, value) ->
                val spannableString = SpannableString("$key\t-> ${"%.2f".format(value)}%\n")
                spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, key.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableString.setSpan(ForegroundColorSpan(resources.getColor(R.color.pink1)), 0, key.length +3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableStringBuilder.append(spannableString)
            }
            sumDetail.text = spannableStringBuilder // Changed this line
        }

    }

    private fun resetButtonColors(){
        severityBtn.setBackgroundColor(resources.getColor(R.color.skyBlue))
        etiValueBtn.setBackgroundColor(resources.getColor(R.color.skyBlue))
        etiTypeBtn.setBackgroundColor(resources.getColor(R.color.skyBlue))
        categoryBtn.setBackgroundColor(resources.getColor(R.color.skyBlue))
        subcategoryBtn.setBackgroundColor(resources.getColor(R.color.skyBlue))
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
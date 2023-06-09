 package com.example.gradapp

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.gradapp.models.CorrelationDataModel
import com.example.gradapp.utils.Pref
import com.example.gradapp.utils.Utility
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import ir.mahozad.android.PieChart


 class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var navView: NavigationView
    lateinit var pref: Pref
    lateinit var correlationData: CorrelationDataModel
    lateinit var pieChart: PieChart
    lateinit var pieChart2: PieChart
    lateinit var barChart: BarChart
    lateinit var barChart2: BarChart
    lateinit var pieChart3: PieChart
    lateinit var correlationBtn: Button
    lateinit var summaryBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        pref = Pref()
        pref.removeCorrParameter(applicationContext)
        pref.removeCorrName(applicationContext)
        drawerLayout = findViewById(R.id.drawerLayout)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout,
                R.string.nav_open, R.string.nav_close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView = findViewById(R.id.navView)
        navView.setNavigationItemSelectedListener(this)

        barChart = findViewById(R.id.barChart)
        barChart2 = findViewById(R.id.barChart2)
        pieChart = findViewById<PieChart>(R.id.pieChart)
        pieChart2 = findViewById<PieChart>(R.id.pieChart2)
        pieChart3 = findViewById<PieChart>(R.id.pieChart3)


        correlationBtn = findViewById(R.id.correlationBtn)
        summaryBtn = findViewById(R.id.summaryBtn)


        val queue = Volley.newRequestQueue(applicationContext)


        val url: String = Utility.apiUrl + "/correlation" /*+ "?DNS=" + dns + "&MacAddress=" + macAddress + "&Ip=" + ip*/
        // add other parameters in a similar manner

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.i("mylog1", response.toString())
                // val generalResponse: GeneralResponse = Gson().fromJson(response.toString(), GeneralResponse::class.java)
                correlationData = Gson().fromJson(response.toString(), CorrelationDataModel::class.java)
                if (correlationData.status == "OK") {
                    println("CORRDATA: " + correlationData.data)
                    // Count each Severity level

                    setupPieChartData()


                    setupBarChartData()

                    setupPieChartData2()
                    setupBarChartData2()



                } else {
                    Utility.showAlert(this, "Error")
                }
            },
            { error ->
                Log.i("mylog2", error.message.toString())
            }
        )

        queue.add(stringRequest)



        /*val pieChart = findViewById<PieChart>(R.id.pieChart)
        pieChart.slices = listOf(
            PieChart.Slice(0.2f, ContextCompat.getColor(applicationContext,R.color.pink1)),
            PieChart.Slice(0.4f, ContextCompat.getColor(applicationContext,R.color.turquise1)),
            PieChart.Slice(0.3f, ContextCompat.getColor(applicationContext,R.color.mintGreen1)),
            PieChart.Slice(0.1f,ContextCompat.getColor(applicationContext,R.color.darkBlue2))
        )*/

        correlationBtn.setOnClickListener{
            startActivity(Intent(this, CorrelationListActivity::class.java))
        }

        summaryBtn.setOnClickListener{
            startActivity(Intent(this, SummaryActivity::class.java))
        }

    }

     val Int.px: Float
         get() = this * Resources.getSystem().displayMetrics.density

     val Int.sp: Float
         get() = this * Resources.getSystem().displayMetrics.scaledDensity


     private fun setupPieChartData() {
         val counts = correlationData.data.groupingBy { it.Severity }.eachCount()
         val totalItems = correlationData.data.size.toDouble()

         val percentages = counts.mapValues { (key, value) -> value / totalItems * 100 }

         val colors = mapOf(
             "Major" to ContextCompat.getColor(applicationContext, R.color.darkBlue),
             "Minor" to ContextCompat.getColor(applicationContext, R.color.turquise1),
             "Normal" to ContextCompat.getColor(applicationContext, R.color.mintGreen1),
             "Unknown" to ContextCompat.getColor(applicationContext, R.color.pink1),
             "Critical" to ContextCompat.getColor(applicationContext, R.color.brown1),
             "Warning" to ContextCompat.getColor(applicationContext, R.color.purple1),
             "Normal" to ContextCompat.getColor(applicationContext, R.color.orange1)
         )

         pieChart.slices = mapToSlices(percentages, colors)

         Log.i("mylog", "Percentages: $percentages")
         Log.i("mylog", "Colors: $colors")
     }



     private fun setupBarChartData() {
         val counts = correlationData.data.groupingBy { it.Status }.eachCount()

         // create BarEntry for Bar Group
         val bargroup = ArrayList<BarEntry>()

         // Get an array of the keys (subcategories) to use in the formatter
         val subcategories = counts.keys.toTypedArray()

         // Adding dynamically the BarEntries
         counts.keys.forEachIndexed { index, key ->
             bargroup.add(BarEntry(index.toFloat(), counts[key]!!.toFloat(), key))
         }

         // creating dataset for Bar Group
         val barDataSet = BarDataSet(bargroup, "")
         barDataSet.color = ContextCompat.getColor(this, R.color.darkBlue2)

         val data = BarData(barDataSet)
         barChart.setData(data)

         barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
         // Set the label count to match the number of entries
         barChart.xAxis.labelCount = counts.size
         barChart.xAxis.enableGridDashedLine(5f, 5f, 0f)
         barChart.axisRight.enableGridDashedLine(5f, 5f, 0f)
         barChart.axisLeft.enableGridDashedLine(5f, 5f, 0f)
         barChart.description.isEnabled = false
         barChart.animateY(1000)
         barChart.legend.isEnabled = false
         barChart.setPinchZoom(true)
         barChart.data.setDrawValues(true) // Set this to true to display values on each bar

         // Define a formatter that maps the x-values to their corresponding subcategories
         val formatter = object : ValueFormatter() {
             override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                 return subcategories[value.toInt()]
             }
         }

         // Apply the formatter to the x-axis
         barChart.xAxis.valueFormatter = formatter
     }

     private fun setupPieChartData2() {
         val counts = correlationData.data.groupingBy { it.EtiValue }.eachCount()
         val totalItems = correlationData.data.size.toDouble()

         val percentages = counts.mapValues { (key, value) -> value / totalItems * 100 }

         val colors = mapOf(
             "Error" to ContextCompat.getColor(applicationContext, R.color.darkBlue),
             "Good" to ContextCompat.getColor(applicationContext, R.color.turquise1),
             "High" to ContextCompat.getColor(applicationContext, R.color.mintGreen1),
             "Low" to ContextCompat.getColor(applicationContext, R.color.pink1),
             "Warning" to ContextCompat.getColor(applicationContext, R.color.brown1),
         )

         pieChart2.slices = mapToSlices(percentages, colors)

         Log.i("mylog", "Percentages: $percentages")
         Log.i("mylog", "Colors: $colors")
     }



     private fun setupBarChartData2() {
         val counts = correlationData.data.groupingBy { it.AlertName }.eachCount()

         val bargroup = ArrayList<BarEntry>()

         val subcategories = counts.keys.toTypedArray()

         counts.keys.forEachIndexed { index, key ->
             bargroup.add(BarEntry(index.toFloat(), counts[key]!!.toFloat(), key))
         }

         // creating dataset for Bar Group
         val barDataSet = BarDataSet(bargroup, "")
         barDataSet.color = ContextCompat.getColor(this, R.color.orange1)

         val data = BarData(barDataSet)
         barChart2.setData(data)

         barChart2.xAxis.position = XAxis.XAxisPosition.BOTTOM
         // Set the label count to match the number of entries
         barChart2.xAxis.labelCount = counts.size
         barChart2.xAxis.enableGridDashedLine(5f, 5f, 0f)
         barChart2.axisRight.enableGridDashedLine(5f, 5f, 0f)
         barChart2.axisLeft.enableGridDashedLine(5f, 5f, 0f)
         barChart2.description.isEnabled = false
         barChart2.animateY(1000)
         barChart2.legend.isEnabled = false
         barChart2.setPinchZoom(true)
         barChart2.data.setDrawValues(true) // Set this to true to display values on each bar

         // Define a formatter that maps the x-values to their corresponding subcategories
         val formatter = object : ValueFormatter() {
             override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                 return subcategories[value.toInt()]
             }
         }

         // Apply the formatter to the x-axis
         barChart2.xAxis.valueFormatter = formatter
     }


     private fun setupPieChartData3() {
         val counts = correlationData.data.groupingBy { it.Node }.eachCount()
         val totalItems = correlationData.data.size.toDouble()

         val percentages = counts.mapValues { (key, value) -> value / totalItems * 100 }
         val colors = mapOf(
             "AlienVault" to ContextCompat.getColor(applicationContext, R.color.darkBlue),
             "ArcSight" to ContextCompat.getColor(applicationContext, R.color.turquise1),
             "CryptoLog" to ContextCompat.getColor(applicationContext, R.color.mintGreen1),
             "FortiSIEM" to ContextCompat.getColor(applicationContext, R.color.pink1),
             "IBM QRadar" to ContextCompat.getColor(applicationContext, R.color.brown1),
             "Insight" to ContextCompat.getColor(applicationContext, R.color.pinkyred),
             "LogPoint" to ContextCompat.getColor(applicationContext, R.color.zeytinGreen),
             "LogRhythm" to ContextCompat.getColor(applicationContext, R.color.lavender),
             "OpsBridge" to ContextCompat.getColor(applicationContext, R.color.skyBlue),
             "PRTG" to ContextCompat.getColor(applicationContext, R.color.yellow1),
             "SiteScope" to ContextCompat.getColor(applicationContext, R.color.lightPurple),
             "Solarwinds" to ContextCompat.getColor(applicationContext, R.color.darkRed),
             "Splunk" to ContextCompat.getColor(applicationContext, R.color.fistikGreen),
             "Zabbix" to ContextCompat.getColor(applicationContext, R.color.hardal),
         )

         pieChart3.slices = mapToSlices(percentages, colors)

         Log.i("mylog", "Percentages: $percentages")
         Log.i("mylog", "Colors: $colors")
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

     fun mapToSlices(percentages: Map<String, Double>, colors: Map<String, Int>): List<PieChart.Slice> {
         val slices = mutableListOf<PieChart.Slice>()
         percentages.forEach { (key, value) ->
             colors[key]?.let {
                 slices.add(PieChart.Slice(fraction = value.toFloat() / 100, it, label = key, legend = key))
             }
         }
         return slices
     }




 }


package com.example.gradapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gradapp.utils.Pref
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("HEYYYY")

        //requestWindowFeature(Window.FEATURE_NO_TITLE)
        //window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        Timer().schedule(3000){
            val accessToken: String =
                Pref().getAccessToken(applicationContext)

            if(accessToken.isEmpty()){
                startActivity(Intent(applicationContext, RegisterActivity::class.java))
                finish()
            }
            else{
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()
            }
        }
    }


}
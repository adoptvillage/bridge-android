package com.adoptvillage.bridge.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adoptvillage.bridge.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val prefs=this.getSharedPreferences(getString(R.string.parent_package_name), Context.MODE_PRIVATE)
        val isLoggedIn=prefs.getBoolean(getString(R.string.is_Logged_In),false)

        if(isLoggedIn){
            startActivity(Intent(this, DashboardActivity::class.java))
        }
        else{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}


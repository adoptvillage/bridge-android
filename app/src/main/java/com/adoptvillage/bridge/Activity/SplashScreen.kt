package com.adoptvillage.bridge.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.DashboardActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val prefs=this.getSharedPreferences(getString(R.string.parent_package_name), Context.MODE_PRIVATE)
        val isLoggedIn=prefs.getBoolean(getString(R.string.is_Logged_In),false)

         val countdownTimer = object : CountDownTimer(2000, 1000) {
             override fun onTick(millisUntilFinished: Long) {}
             override fun onFinish() {
                 callIntent(isLoggedIn)
             }
         }
        countdownTimer.start()
    }

    private fun callIntent(loggedIn: Boolean) {
        if(loggedIn){
            startActivity(Intent(this, DashboardActivity::class.java))
        }
        else{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}


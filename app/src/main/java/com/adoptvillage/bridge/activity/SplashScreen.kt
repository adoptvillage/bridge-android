package com.adoptvillage.bridge.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.service.RetrofitClient
import com.google.firebase.auth.FirebaseAuth

/* Splash Screen
Wait for 2 sec
Check loggedIn or not
 */

class SplashScreen : AppCompatActivity() {
    var idTokenn=""
    lateinit var mAuth: FirebaseAuth
    lateinit var countdownTimer:CountDownTimer
    var isSplashScreenDone=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        //shared Preference
        val prefs=this.getSharedPreferences(
            getString(R.string.parent_package_name),
            Context.MODE_PRIVATE
        )
        mAuth= FirebaseAuth.getInstance()
        //Logged in or not
        val isLoggedIn=prefs.getBoolean(getString(R.string.is_Logged_In), false)

        //countdown timer for 2 sec
        countdownTimer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                callIntent(isLoggedIn)
            }
        }
        countdownTimer.start()
    }
    private fun getIDToken() {
        idTokenn = ""
        mAuth.currentUser!!.getIdToken(true).addOnCompleteListener {
            if (it.isSuccessful) {
                idTokenn = it.result!!.token!!
                RetrofitClient.instance.idToken = idTokenn
                Log.i("Testing",idTokenn)
            }
        }
    }

    //calling next activity
    private fun callIntent(loggedIn: Boolean) {
        if (!isSplashScreenDone) {
            if (loggedIn) {
                getIDToken()
                goToDashboard()
            } else {
                goToAuth()
            }
        }
    }

    private fun goToAuth() {
        isSplashScreenDone=true
        val intent=Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun goToDashboard() {
        isSplashScreenDone=true
        val intent=Intent(this, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if (!isSplashScreenDone) {
            countdownTimer.start()
        }
    }

}



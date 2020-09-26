package com.adoptvillage.bridge.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adoptvillage.bridge.fragment.LogInFragment
import com.adoptvillage.bridge.R

/* Main Activity
Contains 2 fragment -
Login Fragment
SignUp fragment

called from Splash Screen
 */

//colors
const val systemViolet = "#5856D6"
const val systemGray = "#95E2E2E2"
const val systemDarkGray = "#e2e2e2"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //making Login Fragment as default fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment_main, LogInFragment()).commit()
    }
}


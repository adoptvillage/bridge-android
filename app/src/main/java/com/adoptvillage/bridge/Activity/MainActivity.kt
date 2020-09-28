package com.adoptvillage.bridge.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adoptvillage.bridge.fragment.LogInFragment
import com.adoptvillage.bridge.R

const val systemViolet = "#5856D6"
const val systemGray = "#95E2E2E2"
const val systemDarkGray = "#e2e2e2"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_main, LogInFragment()).commit()
    }
}


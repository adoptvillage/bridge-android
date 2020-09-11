package com.adoptvillage.bridge.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adoptvillage.bridge.R
import kotlinx.android.synthetic.main.activity_dashboard.*

private lateinit var prefs:SharedPreferences

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        prefs=this.getSharedPreferences(getString(R.string.parent_package_name), Context.MODE_PRIVATE)

        btnLogoutSetOnClickListener()

    }

    private fun btnLogoutSetOnClickListener() {
        btnLogout.setOnClickListener {
            prefs.edit().putBoolean(getString(R.string.is_Logged_In),false).apply()
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}


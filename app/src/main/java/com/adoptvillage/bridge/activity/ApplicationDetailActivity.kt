package com.adoptvillage.bridge.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adoptvillage.bridge.R
import kotlinx.android.synthetic.main.activity_application_detail.*

class ApplicationDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_detail)

        tvAppDetailsbackSetOnClickListener()

    }

    private fun tvAppDetailsbackSetOnClickListener()
    {
        tvAppDetailsBack.setOnClickListener {
            finish()
        }
    }
}
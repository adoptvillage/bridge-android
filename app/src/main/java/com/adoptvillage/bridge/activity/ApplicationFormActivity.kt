package com.adoptvillage.bridge.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.fragment.applicationFormFragment.ApplicationFormStudentDetails
import com.adoptvillage.bridge.fragment.authFragment.SignUpFragment

class ApplicationFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_form)

        supportFragmentManager.beginTransaction().replace(
            R.id.clAFAFullScreen,
            ApplicationFormStudentDetails()
        ).commit()
    }
}
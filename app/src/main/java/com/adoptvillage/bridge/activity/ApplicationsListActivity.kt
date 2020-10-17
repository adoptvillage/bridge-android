package com.adoptvillage.bridge.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.adapters.ApplicationListAdapter
import com.adoptvillage.bridge.fragment.applicationListFragment.ApplicationsListFragment
import com.adoptvillage.bridge.models.applicationModels.ApplicationResponse

class ApplicationsListActivity : AppCompatActivity() {

    val APPLICATIONTAG="APPLICATIONTAG"
    lateinit var applicationListAdapter:ApplicationListAdapter

    companion object {
        var applicationList= mutableListOf<ApplicationResponse>()
        var selectedApplicationNumber=-1
        var fragnumber=0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applications_list)

        supportFragmentManager.beginTransaction().replace(R.id.fl_wrapper_applications, ApplicationsListFragment()).commit()

    }

}
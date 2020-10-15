package com.adoptvillage.bridge.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.adapters.ApplicationListAdapter
import com.adoptvillage.bridge.fragment.applicationListFragment.ApplicationsListFragment
import com.adoptvillage.bridge.fragment.homeFragment.HomeFragment
import com.adoptvillage.bridge.models.ApplicationResponse
import com.adoptvillage.bridge.models.LocationDataModel
import com.adoptvillage.bridge.service.RetrofitClient
import kotlinx.android.synthetic.main.activity_applications_list.*
import kotlinx.android.synthetic.main.fragment_applications_list.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplicationsListActivity : AppCompatActivity() {

    val APPLICATIONTAG="APPLICATIONTAG"
    lateinit var applicationListAdapter:ApplicationListAdapter

    companion object {
        var applicationList= mutableListOf<ApplicationResponse>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applications_list)

        supportFragmentManager.beginTransaction().replace(R.id.fl_wrapper_applications, ApplicationsListFragment()).commit()

    }


}
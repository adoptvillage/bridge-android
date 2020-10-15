package com.adoptvillage.bridge.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.adapters.ApplicationListAdapter
import com.adoptvillage.bridge.models.ApplicationResponse
import com.adoptvillage.bridge.models.LocationDataModel
import com.adoptvillage.bridge.service.RetrofitClient
import kotlinx.android.synthetic.main.activity_applications_list.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplicationsListActivity : AppCompatActivity(), onApplicationClicked {

    val APPLICATIONTAG="APPLICATIONTAG"
    lateinit var applicationListAdapter:ApplicationListAdapter

    companion object {
        var applicationList= mutableListOf<ApplicationResponse>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applications_list)

        getApplicationList()
        applicationListAdapter=ApplicationListAdapter(applicationList,this)
        rvApplicationList.adapter = applicationListAdapter
        rvApplicationList.layoutManager = LinearLayoutManager(this)
        rvApplicationList.setHasFixedSize(true)

    }

    private fun getApplicationList() {
        RetrofitClient.instance.applicationService.getApplications()
            .enqueue(object : Callback<MutableList<ApplicationResponse>> {
                override fun onResponse(
                    call: Call<MutableList<ApplicationResponse>>,
                    response: Response<MutableList<ApplicationResponse>>
                ) {
                    if (response.isSuccessful) {
                        applicationList=response.body()!!
                        applicationListAdapter.updateList(applicationList)
                    } else {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Log.i(APPLICATIONTAG, response.toString())
                        Log.i(APPLICATIONTAG, jObjError.getString("message"))
                        toastMaker(jObjError.getString("message"))
                    }
                }

                override fun onFailure(call: Call<MutableList<ApplicationResponse>>, t: Throwable) {
                    Log.i(APPLICATIONTAG, "error" + t.message)
                    toastMaker("Failed To Fetch Profile - " + t.message)
                }
            })
    }
    private fun toastMaker(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onApplicationItemClicked(position: Int) {
        Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show()
    }


}
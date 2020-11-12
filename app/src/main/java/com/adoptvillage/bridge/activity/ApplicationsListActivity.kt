package com.adoptvillage.bridge.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.adapters.ApplicationListAdapter
import com.adoptvillage.bridge.fragment.applicationListFragment.ApplicationsListFragment
import com.adoptvillage.bridge.models.LocationDataModel
import com.adoptvillage.bridge.models.applicationModels.ApplicationResponse
import com.google.gson.Gson
import java.io.IOException

class ApplicationsListActivity : AppCompatActivity() {

    val APPLICATIONTAG="APPLICATIONTAG"
    lateinit var applicationListAdapter:ApplicationListAdapter

    companion object {
        var applicationList= mutableListOf<ApplicationResponse>()
        var selectedApplicationNumber=-1
        var fragnumber=0
        var dataForLocationFrag=0 // 1 - state, 2 - district, 3 - sub-district, 4- village
        var state=""
        var district=""
        var subDistrict=""
        var village=""
        var filterState=""
        var filterDistrict=""
        var filterSubDistrict=""
        var filterVillage=""
        var stateNum=-1
        var districtNum=-1
        var subDistrictNum=-1
        var villageNum=-1
        lateinit var locationDataModel: LocationDataModel
        var isLocationFiltered=false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applications_list)

        supportFragmentManager.beginTransaction().replace(R.id.fl_wrapper_applications, ApplicationsListFragment()).commit()
        val JSONData=getJsonDataFromAsset(this,"statesFull.json")
        ApplicationsListActivity.locationDataModel = Gson().fromJson(JSONData, LocationDataModel::class.java)
    }
    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

}
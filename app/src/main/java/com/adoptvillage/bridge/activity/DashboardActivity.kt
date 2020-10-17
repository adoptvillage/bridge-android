package com.adoptvillage.bridge.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adoptvillage.bridge.fragment.historyFragment.HistoryFragment
import com.adoptvillage.bridge.fragment.profileFragment.ProfileFragment
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.fragment.homeFragment.HomeFragment
import com.adoptvillage.bridge.fragment.homeFragment.LocationFragment
import com.adoptvillage.bridge.models.LocationDataModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.io.IOException

/* DASHBOARD ACTIVITY
contains 3 fragment -
Chat fragment
Home Fragment
Profile Fragment

called from Login Fragment
 */

private lateinit var prefs:SharedPreferences

class DashboardActivity : AppCompatActivity() {


    companion object {
        var DASHBOARDTAG="DASHBOARDTAG"
        var fragmentNumberSaver=1 // 1 - home, 0 - profile, 2 - chat, 3-location fragment, 4- logout
        var dataForLocationFrag=0 // 1 - state, 2 - district, 3 - sub-district, 4- village
        var state="State"
        var district="District"
        var subDistrict="Sub District"
        var village="Village"
        var stateNum=-1
        var districtNum=-1
        var subDistrictNum=-1
        var villageNum=-1
        lateinit var locationDataModel:LocationDataModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        //shared preference
        prefs=this.getSharedPreferences(getString(R.string.parent_package_name), Context.MODE_PRIVATE)

        //function for replacing fragments
        supportFragmentManager.beginTransaction().replace(R.id.fl_wrapper, HomeFragment()).commit()

        //item listener for bottom navigation
        btmNavigationSetOnItemClickListener()

        val JSONData=getJsonDataFromAsset(this,"statesFull.json")
        locationDataModel= Gson().fromJson(JSONData, LocationDataModel::class.java)

    }

    private fun btmNavigationSetOnItemClickListener() {
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.ic_home->{
                    if (fragmentNumberSaver!=1) {
                        supportFragmentManager.popBackStackImmediate()
                        supportFragmentManager.beginTransaction().remove(LocationFragment())
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_wrapper, HomeFragment()).commit()
                        fragmentNumberSaver=1
                    }
                }
                R.id.ic_profile->{
                    if (fragmentNumberSaver!=0) {
                        supportFragmentManager.popBackStack()
                        supportFragmentManager.beginTransaction().replace(R.id.fl_wrapper, ProfileFragment()).commit()
                        fragmentNumberSaver=0
                    }
                }
                R.id.ic_chats->{
                    if (fragmentNumberSaver!=2) {
                        supportFragmentManager.popBackStack()
                        supportFragmentManager.beginTransaction().replace(R.id.fl_wrapper, HistoryFragment()).commit()
                        fragmentNumberSaver=2
                    }
                }
            }
            true
        }
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



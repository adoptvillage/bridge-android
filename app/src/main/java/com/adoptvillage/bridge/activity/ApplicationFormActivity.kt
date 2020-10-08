package com.adoptvillage.bridge.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.fragment.applicationFormFragment.APPLICATIONFRAGTAG
import com.adoptvillage.bridge.fragment.applicationFormFragment.ApplicationFormStudentDetails
import com.adoptvillage.bridge.models.LocationDataModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_application_form_student_details.*
import java.io.IOException

class ApplicationFormActivity : AppCompatActivity() {
    companion object {
        var dataForLocationFrag=0 // 1 - state, 2 - district, 3 - sub-district, 4- village
        var state="State"
        var district="District"
        var subDistrict="Sub District"
        var village="Village"
        var stateNum=-1
        var districtNum=-1
        var subDistrictNum=-1
        var villageNum=-1
        lateinit var locationDataModel: LocationDataModel
        var isLocationSelected=false
        var studentFirstName=""
        var studentLastName=""
        var studentAadhaarNumber=""
        var studentContactNumber=""
        var instituteName=""
        var instituteAffCode=""
        var instituteState=""
        var instituteDistrict=""
        var instituteCourse=""
        var instituteSemester=""
        var instituteFeesAmount=""

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_form)

        supportFragmentManager.beginTransaction().replace(
            R.id.clAFAFullScreen,
            ApplicationFormStudentDetails()
        ).commit()
        var JSONData=getJsonDataFromAsset(this,"states.json")
        locationDataModel = Gson().fromJson(JSONData, LocationDataModel::class.java)
    }
    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    override fun onPause() {
        super.onPause()
        Log.i(APPLICATIONFRAGTAG,"onPause")
        studentFirstName=""
        studentLastName=""
        studentAadhaarNumber=""
        studentContactNumber=""
        dataForLocationFrag=0 // 1 - state, 2 - district, 3 - sub-district, 4- village
        state="State"
        district="District"
        subDistrict="Sub District"
        village="Village"
        stateNum=-1
        districtNum=-1
        subDistrictNum=-1
        villageNum=-1
        isLocationSelected=false
        instituteName=""
        instituteAffCode=""
        instituteState=""
        instituteDistrict=""
        instituteCourse=""
        instituteSemester=""
        instituteFeesAmount=""
    }

    override fun onStop() {
        super.onStop()
        Log.i(APPLICATIONFRAGTAG,"onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(APPLICATIONFRAGTAG,"onDestroy")
    }
}
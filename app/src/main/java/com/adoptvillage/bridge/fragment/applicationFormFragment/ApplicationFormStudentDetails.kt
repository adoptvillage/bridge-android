package com.adoptvillage.bridge.fragment.applicationFormFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.ApplicationFormActivity
import kotlinx.android.synthetic.main.fragment_application_form_student_details.*


val APPLICATIONFRAGTAG="APPLICATIONFRAGTAG"
class ApplicationFormStudentDetails : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.explode)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_application_form_student_details,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAppSDNextSetOnClickListener()
        tvLocationShowerSetOnClickListener()
        getDataFromActivity()

    }

    private fun getDataFromActivity() {
        etStudentFirstName.setText(ApplicationFormActivity.studentFirstName)
        etStudentLastName.setText(ApplicationFormActivity.studentLastName)
        etStudentAadhaarNumber.setText(ApplicationFormActivity.studentAadhaarNumber)
        etStudentContactNumber.setText(ApplicationFormActivity.studentContactNumber)
        etStudentPurpose.setText(ApplicationFormActivity.studentPurpose)

        if(ApplicationFormActivity.stateNum!=-1 && ApplicationFormActivity.districtNum!=-1 && ApplicationFormActivity.subDistrictNum!=-1 && ApplicationFormActivity.villageNum!=-1) {
            val location=ApplicationFormActivity.state+", "+ApplicationFormActivity.district+", "+ApplicationFormActivity.subDistrict+", "+ApplicationFormActivity.village
            tvLocationShower.text=location
        }

    }

    private fun tvLocationShowerSetOnClickListener() {
        tvLocationShower.setOnClickListener {
            ApplicationFormActivity.studentFirstName=etStudentFirstName.text.toString()
            ApplicationFormActivity.studentLastName=etStudentLastName.text.toString()
            ApplicationFormActivity.studentAadhaarNumber=etStudentAadhaarNumber.text.toString()
            ApplicationFormActivity.studentContactNumber=etStudentContactNumber.text.toString()
            ApplicationFormActivity.studentPurpose=etStudentPurpose.text.toString()
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.clAFAFullScreen, LocationStudentDetailFragment())?.addToBackStack(javaClass.name)?.commit()
        }
    }

    private fun toastMaker(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
    private fun validateData():Boolean {
        return if(etStudentFirstName.text.isNullOrEmpty() || etStudentFirstName.text.isNullOrBlank()) {
            toastMaker("First Name cannot be Empty")
            Log.i(APPLICATIONFRAGTAG, "First Name cannot be Empty")
            false
        } else if(etStudentLastName.text.isNullOrEmpty() || etStudentLastName.text.isNullOrBlank()) {
            toastMaker("Last Name cannot be Empty")
            Log.i(APPLICATIONFRAGTAG, "Last Name cannot be Empty")
            false
        } else if(etStudentAadhaarNumber.text.isNullOrEmpty() || etStudentAadhaarNumber.text.isNullOrBlank()){
            toastMaker("Aadhaar Number can not be Empty")
            Log.i(APPLICATIONFRAGTAG, "Aadhaar Number can not be Empty")
            false
        } else if(etStudentAadhaarNumber.text.length!=12){
            toastMaker("Invalid Aadhaar Number")
            Log.i(APPLICATIONFRAGTAG, "Invalid Aadhaar Number")
            false
        } else if (etStudentContactNumber.text.isNullOrEmpty() || etStudentContactNumber.text.isNullOrBlank()){
            toastMaker("Contact Number cannot be empty")
            Log.i(APPLICATIONFRAGTAG, "Contact Number cannot be empty")
            false
        } else if (!ApplicationFormActivity.isLocationSelected){
            toastMaker("Select Location")
            Log.i(APPLICATIONFRAGTAG, "Select Location")
            false
        }
        else{
            true
        }
    }

    private fun btnAppSDNextSetOnClickListener() {
        btnAppSDNext.setOnClickListener {
            if (validateData()) {
                Log.i("test",ApplicationFormActivity.state+", "+ApplicationFormActivity.district+", "+ApplicationFormActivity.subDistrict+", "+ApplicationFormActivity.village)
                ApplicationFormActivity.studentFirstName=etStudentFirstName.text.toString()
                ApplicationFormActivity.studentLastName=etStudentLastName.text.toString()
                ApplicationFormActivity.studentAadhaarNumber=etStudentAadhaarNumber.text.toString()
                ApplicationFormActivity.studentContactNumber=etStudentContactNumber.text.toString()
                ApplicationFormActivity.studentPurpose=etStudentPurpose.text.toString()
                Log.i(APPLICATIONFRAGTAG,ApplicationFormActivity.studentFirstName)
                Log.i(APPLICATIONFRAGTAG,ApplicationFormActivity.studentLastName)
                Log.i(APPLICATIONFRAGTAG,ApplicationFormActivity.studentAadhaarNumber)
                Log.i(APPLICATIONFRAGTAG,ApplicationFormActivity.studentContactNumber)
                Log.i(APPLICATIONFRAGTAG,ApplicationFormActivity.state+" "+ApplicationFormActivity.district+" "+ApplicationFormActivity.subDistrict+" "+ApplicationFormActivity.village)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.clAFAFullScreen, ApplicationFormInstituteDetails())?.commit()
            }
        }
    }
}


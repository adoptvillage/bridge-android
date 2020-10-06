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
import com.adoptvillage.bridge.fragment.homeFragment.LocationFragment
import kotlinx.android.synthetic.main.fragment_application_form_institute_details.*
import kotlinx.android.synthetic.main.fragment_application_form_student_details.*


class ApplicationFormInstituteDetails : Fragment() {

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
            R.layout.fragment_application_form_institute_details,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAppIDNext.setOnClickListener {
            if (validateData()) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.clAFAFullScreen, ApplicationFormDocuments())?.commit()
            }
        }
    }
    private fun validateData():Boolean {
        return if(etInsName.text.isNullOrEmpty() || etInsName.text.isNullOrBlank()) {
            toastMaker("Name cannot be Empty")
            Log.i(APPLICATIONFRAGTAG, "Name cannot be Empty")
            false
        } else if(etInsAffCode.text.isNullOrEmpty() || etInsAffCode.text.isNullOrBlank()) {
            toastMaker("Affiliation code cannot be Empty")
            Log.i(APPLICATIONFRAGTAG, "Affiliation code cannot be Empty")
            false
        } else if(etInsAddress.text.isNullOrEmpty() || etInsAddress.text.isNullOrBlank()){
            toastMaker("Address cannot be Empty")
            Log.i(APPLICATIONFRAGTAG, "Address cannot be Empty")
            false
        } else if (etInsLocation.text.isNullOrEmpty() || etInsLocation.text.isNullOrBlank()){
            toastMaker("Location cannot be empty")
            Log.i(APPLICATIONFRAGTAG, "Location cannot be empty")
            false
        } else if(etInsCourse.text.isNullOrEmpty() || etInsCourse.text.isNullOrBlank()) {
            toastMaker("Course cannot be Empty")
            Log.i(APPLICATIONFRAGTAG, "Course cannot be Empty")
            false
        } else if(etInsSemester.text.isNullOrEmpty() || etInsSemester.text.isNullOrBlank()){
            toastMaker("Semester cannot be Empty")
            Log.i(APPLICATIONFRAGTAG, "Semester cannot be Empty")
            false
        } else if (etInsFeeAmount.text.isNullOrEmpty() || etInsFeeAmount.text.isNullOrBlank()){
            toastMaker("Fee Amount cannot be empty")
            Log.i(APPLICATIONFRAGTAG, "Fee Amount cannot be empty")
            false
        } else{
            true
        }
    }
    private fun toastMaker(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

}
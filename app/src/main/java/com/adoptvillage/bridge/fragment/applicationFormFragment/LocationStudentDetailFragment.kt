package com.adoptvillage.bridge.fragment.applicationFormFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.ApplicationFormActivity
import kotlinx.android.synthetic.main.fragment_location_student_detail.*


class LocationStudentDetailFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_location_student_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
        updateDataOnScreen()
        updateSetOnClickListener()
    }
    private fun updateSetOnClickListener() {
        btnAppSDLUpdate.setOnClickListener {
            if(ApplicationFormActivity.stateNum!=-1 && ApplicationFormActivity.districtNum!=-1 && ApplicationFormActivity.subDistrictNum!=-1 && ApplicationFormActivity.villageNum!=-1) {
                ApplicationFormActivity.isLocationSelected = true
            }
            activity?.supportFragmentManager?.popBackStackImmediate()
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.clAFAFullScreen, ApplicationFormStudentDetails())?.commit()
        }
    }
    private fun updateDataOnScreen() {
        tvAppSDLState.text=ApplicationFormActivity.state
        tvAppSDLDistrict.text=ApplicationFormActivity.district
        tvAppSDLSubDistrict.text=ApplicationFormActivity.subDistrict
        tvAppSDLVillage.text=ApplicationFormActivity.village
    }
    private fun clickListeners() {
        clAppSDLState.setOnClickListener {
            ApplicationFormActivity.dataForLocationFrag=1

            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.clAFAFullScreen, LocationListingStudentDetailFragment())?.addToBackStack(javaClass.name)?.commit()
        }
        clAppSDLDistrict.setOnClickListener {
            if (ApplicationFormActivity.stateNum!=-1) {
                ApplicationFormActivity.dataForLocationFrag = 2

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.clAFAFullScreen, LocationListingStudentDetailFragment())
                    ?.addToBackStack(javaClass.name)?.commit()
            }
        }
        clAppSDLSubDistrict.setOnClickListener {
            if (ApplicationFormActivity.districtNum!=-1) {
                ApplicationFormActivity.dataForLocationFrag = 3

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.clAFAFullScreen, LocationListingStudentDetailFragment())
                    ?.addToBackStack(javaClass.name)?.commit()
            }
        }
        clAppSDLVillage.setOnClickListener {
            if (ApplicationFormActivity.subDistrictNum!=-1) {
                ApplicationFormActivity.dataForLocationFrag = 4

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.clAFAFullScreen, LocationListingStudentDetailFragment())
                    ?.addToBackStack(javaClass.name)?.commit()
            }
        }
    }

}


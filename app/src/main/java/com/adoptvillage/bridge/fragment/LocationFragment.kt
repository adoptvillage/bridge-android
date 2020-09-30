package com.adoptvillage.bridge.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.DashboardActivity
import kotlinx.android.synthetic.main.fragment_location.*

class LocationFragment : Fragment() {


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
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DashboardActivity.fragmentNumberSaver=3

        clickListeners()
        updateDataOnScreen()
        updateSetOnClickListener()
    }

    private fun updateSetOnClickListener() {
        btnLSUpdate.setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fl_wrapper, HomeFragment())?.commit()
        }
    }

    private fun updateDataOnScreen() {
        tvLSState.text=DashboardActivity.state
        tvLSDistrict.text=DashboardActivity.district
        tvLSSubDistrict.text=DashboardActivity.subDistrict
        tvLSVillage.text=DashboardActivity.village
    }

    private fun clickListeners() {
        clLSState.setOnClickListener {
            DashboardActivity.dataForLocationFrag=1
            //activity?.supportFragmentManager?.popBackStackImmediate()
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fl_wrapper, LocationListingFragment())?.addToBackStack(javaClass.name)?.commit()
        }
        clLSDistrict.setOnClickListener {
            if (DashboardActivity.stateNum!=-1) {
                DashboardActivity.dataForLocationFrag = 2
                //activity?.supportFragmentManager?.popBackStackImmediate()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fl_wrapper, LocationListingFragment())
                    ?.addToBackStack(javaClass.name)?.commit()
            }
        }
        clLSSubDistrict.setOnClickListener {
            if (DashboardActivity.districtNum!=-1) {
                DashboardActivity.dataForLocationFrag = 3
                //activity?.supportFragmentManager?.popBackStackImmediate()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fl_wrapper, LocationListingFragment())
                    ?.addToBackStack(javaClass.name)?.commit()
            }
        }
        clLSVillage.setOnClickListener {
            if (DashboardActivity.subDistrictNum!=-1) {
                DashboardActivity.dataForLocationFrag = 4
                //activity?.supportFragmentManager?.popBackStackImmediate()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fl_wrapper, LocationListingFragment())
                    ?.addToBackStack(javaClass.name)?.commit()
            }
        }
    }
}


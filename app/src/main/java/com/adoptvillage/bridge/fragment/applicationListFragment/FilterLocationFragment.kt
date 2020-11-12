package com.adoptvillage.bridge.fragment.applicationListFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.ApplicationsListActivity
import kotlinx.android.synthetic.main.fragment_filter_location.*


class FilterLocationFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_filter_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ApplicationsListActivity.fragnumber=3
        clickListeners()
        updateDataOnScreen()
        btnFilterSetOnClickListener()
    }
    private fun btnFilterSetOnClickListener() {
        btnFLUpdate.setOnClickListener {
            if (ApplicationsListActivity.stateNum!=-1){
                ApplicationsListActivity.filterState=""
                ApplicationsListActivity.filterDistrict=""
                ApplicationsListActivity.filterSubDistrict=""
                ApplicationsListActivity.filterVillage=""
                ApplicationsListActivity.filterState=ApplicationsListActivity.state
                ApplicationsListActivity.filterDistrict=ApplicationsListActivity.district
                ApplicationsListActivity.filterSubDistrict=ApplicationsListActivity.subDistrict
                ApplicationsListActivity.filterVillage=ApplicationsListActivity.village
                ApplicationsListActivity.isLocationFiltered=true
                toApplicationListFragment()
            }
        }
    }
    private fun toastMaker(message: String?) {
        if(ApplicationsListActivity.fragnumber==3){
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun toApplicationListFragment() {
        activity?.supportFragmentManager?.popBackStackImmediate()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fl_wrapper_applications, ApplicationsListFragment())?.commit()
    }

    private fun updateDataOnScreen() {
        tvFLState.text= ApplicationsListActivity.state
        tvFLDistrict.text= ApplicationsListActivity.district
        tvFLSubDistrict.text= ApplicationsListActivity.subDistrict
        tvFLVillage.text= ApplicationsListActivity.village
    }

    private fun clickListeners() {
        clFLState.setOnClickListener {
            ApplicationsListActivity.dataForLocationFrag=1

            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fl_wrapper_applications, FilterLocationListingFragment())?.addToBackStack(javaClass.name)?.commit()
        }
        clFLDistrict.setOnClickListener {
            if (ApplicationsListActivity.stateNum!=-1) {
                ApplicationsListActivity.dataForLocationFrag = 2

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fl_wrapper_applications, FilterLocationListingFragment())
                    ?.addToBackStack(javaClass.name)?.commit()
            }
        }
        clFLSubDistrict.setOnClickListener {
            if (ApplicationsListActivity.districtNum!=-1) {
                ApplicationsListActivity.dataForLocationFrag = 3

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fl_wrapper_applications, FilterLocationListingFragment())
                    ?.addToBackStack(javaClass.name)?.commit()
            }
        }
        clFLVillage.setOnClickListener {
            if (ApplicationsListActivity.subDistrictNum!=-1) {
                ApplicationsListActivity.dataForLocationFrag = 4

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fl_wrapper_applications, FilterLocationListingFragment())
                    ?.addToBackStack(javaClass.name)?.commit()
            }
        }
    }
}
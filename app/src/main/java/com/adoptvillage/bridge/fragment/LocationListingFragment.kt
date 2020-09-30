package com.adoptvillage.bridge.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.adapters.AreaListingAdapter
import kotlinx.android.synthetic.main.fragment_location_listing.*

class LocationListingFragment : Fragment(),CellClickListener {

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
        return inflater.inflate(R.layout.fragment_location_listing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvLLSLister.layoutManager = LinearLayoutManager(context)

        val dataForAdapter=getDataForAdapter()
        rvLLSLister.adapter = AreaListingAdapter(context!!, dataForAdapter,this)

    }

    private fun getDataForAdapter(): MutableList<String?> {
        val dataToReturn= mutableListOf<String?>()
        val baseObj=DashboardActivity.locationDataModel.states
        when (DashboardActivity.dataForLocationFrag) {
            1 -> {
                //States
                tvLLSName.text=activity?.getString(R.string.state)
                for (element in baseObj!!){
                    dataToReturn.add(element!!.state)
                }
            }
            2 -> {
                //district
                tvLLSName.text=activity?.getString(R.string.district)
                for (element in baseObj!![DashboardActivity.stateNum]!!.districts!!){
                    dataToReturn.add(element!!.district)
                }
            }
            3 -> {
                //subdistrict
                tvLLSName.text=activity?.getString(R.string.sub_district)
                for (element in baseObj!![DashboardActivity.stateNum]!!.districts!![DashboardActivity.districtNum]!!.subDistricts!!){
                    dataToReturn.add(element!!.subDistrict)
                }
            }
            4 -> {
                //Villages
                tvLLSName.text=activity?.getString(R.string.village)
                for (element in baseObj!![DashboardActivity.stateNum]!!.districts!![DashboardActivity.districtNum]!!.subDistricts!![DashboardActivity.subDistrictNum]!!.villages!!){
                    dataToReturn.add(element)
                }
            }
        }
        return dataToReturn
    }

    override fun onCellClickListener() {
        //Toast.makeText(context,"Cell clicked", Toast.LENGTH_SHORT).show()
        activity?.supportFragmentManager?.popBackStackImmediate()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fl_wrapper, LocationFragment())?.commit()
    }
}

interface CellClickListener {
    fun onCellClickListener()
}


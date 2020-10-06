package com.adoptvillage.bridge.fragment.applicationFormFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.ApplicationFormActivity
import com.adoptvillage.bridge.adapters.AreaListingAdapterStudentDetail
import kotlinx.android.synthetic.main.fragment_location_listing_student_detail.*
import java.util.*


class LocationListingStudentDetailFragment : Fragment(), CellClickListener {

    lateinit var areaListingAdapter: AreaListingAdapterStudentDetail

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
        return inflater.inflate(R.layout.fragment_location_listing_student_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvAppSDLLSLister.layoutManager = LinearLayoutManager(context)

        val dataForAdapter=getDataForAdapter()
        areaListingAdapter= AreaListingAdapterStudentDetail(context!!, dataForAdapter, this)
        rvAppSDLLSLister.adapter=areaListingAdapter
        svAppSDLLSSearchingSetOnQueryTextListener(dataForAdapter)
    }
    private fun svAppSDLLSSearchingSetOnQueryTextListener(dataForAdapter: MutableList<String?>) {
        svAppSDLLSSearching.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank() && !query.isNullOrEmpty()) {
                    filterData(dataForAdapter, query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank() && !newText.isNullOrEmpty()) {
                    filterData(dataForAdapter, newText)
                }
                return true
            }

        })
    }

    private fun filterData(dataForAdapter: MutableList<String?>, query: String?) {
        val updatedData= mutableListOf<String?>()
        for (str in dataForAdapter) {
            val strLowerCase = str?.toLowerCase(Locale.ROOT)
            if (strLowerCase!!.contains(query!!.toRegex())) {
                updatedData.add(str)
            }
        }

        areaListingAdapter.updateList(updatedData)
    }

    private fun getDataForAdapter(): MutableList<String?> {
        val dataToReturn= mutableListOf<String?>()
        val baseObj= ApplicationFormActivity.locationDataModel.states
        when (ApplicationFormActivity.dataForLocationFrag) {
            1 -> {
                //States
                tvAppSDLLSName.text = activity?.getString(R.string.state)
                for (element in baseObj!!) {
                    dataToReturn.add(element!!.state)
                }
            }
            2 -> {
                //district
                tvAppSDLLSName.text = activity?.getString(R.string.district)
                for (element in baseObj!![ApplicationFormActivity.stateNum]!!.districts!!) {
                    dataToReturn.add(element!!.district)
                }
            }
            3 -> {
                //subdistrict
                tvAppSDLLSName.text = activity?.getString(R.string.sub_district)
                for (element in baseObj!![ApplicationFormActivity.stateNum]!!.districts!![ApplicationFormActivity.districtNum]!!.subDistricts!!) {
                    dataToReturn.add(element!!.subDistrict)
                }
            }
            4 -> {
                //Villages
                tvAppSDLLSName.text = activity?.getString(R.string.village)
                for (element in baseObj!![ApplicationFormActivity.stateNum]!!.districts!![ApplicationFormActivity.districtNum]!!.subDistricts!![ApplicationFormActivity.subDistrictNum]!!.villages!!) {
                    dataToReturn.add(element)
                }
            }
        }
        return dataToReturn
    }

    override fun onCellClickListener() {

        activity?.supportFragmentManager?.popBackStackImmediate()
        activity?.supportFragmentManager?.beginTransaction()?.replace(
            R.id.clAFAFullScreen,
            LocationStudentDetailFragment()
        )?.commit()
    }
}

interface CellClickListener {
    fun onCellClickListener()
}

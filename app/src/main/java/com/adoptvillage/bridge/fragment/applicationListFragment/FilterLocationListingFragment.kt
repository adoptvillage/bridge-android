package com.adoptvillage.bridge.fragment.applicationListFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.ApplicationsListActivity
import com.adoptvillage.bridge.adapters.FilterLocationListingAdapter
import kotlinx.android.synthetic.main.fragment_filter_location_listing.*
import java.util.*


class FilterLocationListingFragment : Fragment(), CellClickListener {
    lateinit var areaListingAdapter: FilterLocationListingAdapter
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
        return inflater.inflate(R.layout.fragment_filter_location_listing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvFLLLister.layoutManager = LinearLayoutManager(context)

        ApplicationsListActivity.fragnumber=4
        val dataForAdapter=getDataForAdapter()
        areaListingAdapter= FilterLocationListingAdapter(context!!, dataForAdapter, this)
        rvFLLLister.adapter=areaListingAdapter
        svFLLSearchingSetOnQueryTextListener(dataForAdapter)
    }
    private fun svFLLSearchingSetOnQueryTextListener(dataForAdapter: MutableList<String?>) {
        svFLLSearching.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        val baseObj= ApplicationsListActivity.locationDataModel.states
        when (ApplicationsListActivity.dataForLocationFrag) {
            1 -> {
                //States
                tvFLLName.text = activity?.getString(R.string.state)
                for (element in baseObj!!) {
                    dataToReturn.add(element!!.state)
                }
            }
            2 -> {
                //district
                tvFLLName.text = activity?.getString(R.string.district)
                for (element in baseObj!![ApplicationsListActivity.stateNum]!!.districts!!) {
                    dataToReturn.add(element!!.district)
                }
            }
            3 -> {
                //subDistrict
                tvFLLName.text = activity?.getString(R.string.sub_district)
                for (element in baseObj!![ApplicationsListActivity.stateNum]!!.districts!![ApplicationsListActivity.districtNum]!!.subDistricts!!) {
                    dataToReturn.add(element!!.subDistrict)
                }
            }
            4 -> {
                //Villages
                tvFLLName.text = activity?.getString(R.string.village)
                for (element in baseObj!![ApplicationsListActivity.stateNum]!!.districts!![ApplicationsListActivity.districtNum]!!.subDistricts!![ApplicationsListActivity.subDistrictNum]!!.villages!!) {
                    dataToReturn.add(element)
                }
            }
        }
        return dataToReturn
    }

    override fun onCellClickListener() {
        activity?.supportFragmentManager?.popBackStackImmediate()
        activity?.supportFragmentManager?.beginTransaction()?.replace(
            R.id.fl_wrapper_applications,
            FilterLocationFragment()
        )?.commit()
    }
}

interface CellClickListener {
    fun onCellClickListener()
}



package com.adoptvillage.bridge.fragment.homeFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.models.PrefLocationModel
import com.adoptvillage.bridge.models.UpdateLocationDefaultResponse
import com.adoptvillage.bridge.service.RetrofitClient
import kotlinx.android.synthetic.main.fragment_location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            if (DashboardActivity.stateNum!=-1 && DashboardActivity.districtNum!=-1 && DashboardActivity.subDistrictNum!=-1 && DashboardActivity.villageNum!=-1) {
                pbAVLocation?.visibility=View.VISIBLE
                val obj = PrefLocationModel(
                    DashboardActivity.state,
                    DashboardActivity.district,
                    DashboardActivity.subDistrict,
                    DashboardActivity.village
                )
                RetrofitClient.instance.dashboardService.updateLocation(obj).enqueue(object :
                    Callback<UpdateLocationDefaultResponse> {
                    override fun onResponse(
                        call: Call<UpdateLocationDefaultResponse>,
                        response: Response<UpdateLocationDefaultResponse>
                    ) {
                        if (response.isSuccessful) {
                            toastMaker(response.body()?.message)
                            toHomeFragment()
                        } else {
                            toastMaker("Location update failed")
                            toHomeFragment()
                        }
                    }
                    override fun onFailure(
                        call: Call<UpdateLocationDefaultResponse>,
                        t: Throwable
                    ) {
                        toastMaker("Location update failed")
                        toHomeFragment()
                    }
                })
            }else{
                toastMaker("Village not selected")
            }
        }
    }
    private fun toastMaker(message: String?) {
        if(DashboardActivity.fragmentNumberSaver==3){
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun toHomeFragment() {
        pbAVLocation?.visibility=View.INVISIBLE
        activity?.supportFragmentManager?.popBackStackImmediate()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fl_wrapper, HomeFragment())?.commit()
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

            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fl_wrapper, LocationListingFragment())?.addToBackStack(javaClass.name)?.commit()
        }
        clLSDistrict.setOnClickListener {
            if (DashboardActivity.stateNum!=-1) {
                DashboardActivity.dataForLocationFrag = 2

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fl_wrapper, LocationListingFragment())
                    ?.addToBackStack(javaClass.name)?.commit()
            }
        }
        clLSSubDistrict.setOnClickListener {
            if (DashboardActivity.districtNum!=-1) {
                DashboardActivity.dataForLocationFrag = 3

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fl_wrapper, LocationListingFragment())
                    ?.addToBackStack(javaClass.name)?.commit()
            }
        }
        clLSVillage.setOnClickListener {
            if (DashboardActivity.subDistrictNum!=-1) {
                DashboardActivity.dataForLocationFrag = 4

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fl_wrapper, LocationListingFragment())
                    ?.addToBackStack(javaClass.name)?.commit()
            }
        }
    }
}


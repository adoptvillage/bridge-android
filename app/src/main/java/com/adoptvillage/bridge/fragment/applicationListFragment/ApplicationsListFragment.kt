package com.adoptvillage.bridge.fragment.applicationListFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.ApplicationsListActivity
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.adapters.ApplicationListAdapter
import com.adoptvillage.bridge.fragment.profileFragment.LocationFragment
import com.adoptvillage.bridge.models.applicationModels.FilterApplicationModel
import com.adoptvillage.bridge.models.applicationModels.ApplicationResponse
import com.adoptvillage.bridge.service.RetrofitClient
import kotlinx.android.synthetic.main.fragment_applications_list.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ApplicationsListFragment : Fragment(), OnApplicationClicked {

    val APPLICATIONTAG="APPLICATIONTAG"
    lateinit var applicationListAdapter:ApplicationListAdapter

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
        return inflater.inflate(R.layout.fragment_applications_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!ApplicationsListActivity.isLocationFiltered){
            ApplicationsListActivity.filterState=DashboardActivity.state
            ApplicationsListActivity.filterDistrict=DashboardActivity.district
            ApplicationsListActivity.filterSubDistrict=DashboardActivity.subDistrict
            ApplicationsListActivity.filterVillage=DashboardActivity.village
        }
        ApplicationsListActivity.fragnumber=0
        getApplicationList()
        loadApplicationCards()
        tvApplicationListBackSetOnClickListener()
        tvFilterLocationSetOnClickListener()
    }

    private fun tvFilterLocationSetOnClickListener() {
        tvFilterLocation.setOnClickListener {
            ApplicationsListActivity.state=""
            ApplicationsListActivity.district=""
            ApplicationsListActivity.subDistrict=""
            ApplicationsListActivity.village=""
            activity?.supportFragmentManager?.popBackStackImmediate()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fl_wrapper_applications, FilterLocationFragment())?.addToBackStack(javaClass.name)
                ?.commit()
        }
    }

    private fun tvApplicationListBackSetOnClickListener() {
        tvApplicationListBack.setOnClickListener {
            val intent=Intent(context, DashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    private fun getApplicationList() {
        Log.i("test",DashboardActivity.state+DashboardActivity.district+DashboardActivity.subDistrict+DashboardActivity.village)
        val obj= FilterApplicationModel(state = ApplicationsListActivity.filterState,district = ApplicationsListActivity.filterDistrict,subDistrict = ApplicationsListActivity.filterSubDistrict,area = ApplicationsListActivity.filterVillage)
        RetrofitClient.instance.applicationService.getFilteredApplications(obj)
            .enqueue(object : Callback<MutableList<ApplicationResponse>> {
                override fun onResponse(
                    call: Call<MutableList<ApplicationResponse>>,
                    response: Response<MutableList<ApplicationResponse>>
                ) {
                    if (response.isSuccessful) {
                        ApplicationsListActivity.applicationList =response.body()!!
                        applicationListAdapter.updateList(ApplicationsListActivity.applicationList)
                        if (ApplicationsListActivity.applicationList.size==0){
                            toastMaker("No Application found for selected region!")
                        }
                        if (ApplicationsListActivity.fragnumber==0) {
                            if (pbAppList!=null) {
                                pbAppList?.visibility = View.INVISIBLE
                            }
                        }
                    } else {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Log.i(APPLICATIONTAG, response.toString())
                        Log.i(APPLICATIONTAG, jObjError.getString("message"))
                        toastMaker("Failed to Fetch Applications - "+jObjError.getString("message"))
                        if (ApplicationsListActivity.fragnumber==0) {
                            if (pbAppList!=null) {
                                pbAppList?.visibility = View.INVISIBLE
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<MutableList<ApplicationResponse>>, t: Throwable) {
                    Log.i(APPLICATIONTAG, "error" + t.message)
                    toastMaker("No Internet / Server Down")
                    if (ApplicationsListActivity.fragnumber==0) {
                        if (pbAppList!=null) {
                            pbAppList?.visibility = View.INVISIBLE
                        }
                    }
                }
            })
    }

    private fun loadApplicationCards() {
        applicationListAdapter= ApplicationListAdapter(ApplicationsListActivity.applicationList, this)
        rvApplicationList.adapter = applicationListAdapter
        rvApplicationList.layoutManager = LinearLayoutManager(this.context)
        rvApplicationList.setHasFixedSize(true)
    }

    override fun onApplicationItemClicked(position: Int) {
        ApplicationsListActivity.selectedApplicationNumber=position
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fl_wrapper_applications, ApplicationDetail1())?.commit()
    }
    private fun toastMaker(message: String?) {
        if (ApplicationsListActivity.fragnumber==0) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}

interface OnApplicationClicked
{
    fun onApplicationItemClicked(position: Int)
}


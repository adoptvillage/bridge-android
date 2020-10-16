package com.adoptvillage.bridge.fragment.applicationListFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.ApplicationsListActivity
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.fragment.applicationFormFragment.APPLICATIONFRAGTAG
import com.adoptvillage.bridge.models.applicationModels.AcceptApplicationDefaultResponse
import com.adoptvillage.bridge.models.applicationModels.AcceptApplicationModel
import com.adoptvillage.bridge.service.RetrofitClient
import kotlinx.android.synthetic.main.fragment_application_detail_1.*
import kotlinx.android.synthetic.main.fragment_application_detail_2.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ApplicationDetail2 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_application_detail_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvAppDetail2BackSetOnClickListener()
        btnAppDetail2AcceptSetOnClickListener()
    }
    private fun toastMaker(message: String?) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }
    private fun btnAppDetail2AcceptSetOnClickListener() {
        btnAppDetail2Accept.setOnClickListener {
            val moderatorEmail=etModeratorEmailAddress.text.toString()
            val obj= AcceptApplicationModel(donatingFullAmount = true,amount = 100000,moderatorEmail = moderatorEmail,applicationId = ApplicationsListActivity.applicationList[ApplicationsListActivity.selectedApplicationNumber].id)
            RetrofitClient.instance.applicationService.acceptApplication(obj)
                .enqueue(object : Callback<AcceptApplicationDefaultResponse> {
                    override fun onResponse(
                        call: Call<AcceptApplicationDefaultResponse>,
                        response: Response<AcceptApplicationDefaultResponse>
                    ) {
                        if (response.isSuccessful) {
                            toastMaker(response.body()?.message)
                            val intent= Intent(context, DashboardActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        } else {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            Log.i(APPLICATIONFRAGTAG, response.toString())
                            Log.i(APPLICATIONFRAGTAG, jObjError.getString("message"))
                            toastMaker(jObjError.getString("message"))
                        }
                    }

                    override fun onFailure(call: Call<AcceptApplicationDefaultResponse>, t: Throwable) {
                        Log.i(APPLICATIONFRAGTAG, "error" + t.message)
                        toastMaker("Failed To Fetch Profile - " + t.message)
                    }

                })
        }
    }

    private fun tvAppDetail2BackSetOnClickListener() {
        tvAppDetail2Back.setOnClickListener {
            btnAppDetail1Next.setOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fl_wrapper_applications, ApplicationDetail1())?.commit()
            }
        }
    }
 
}
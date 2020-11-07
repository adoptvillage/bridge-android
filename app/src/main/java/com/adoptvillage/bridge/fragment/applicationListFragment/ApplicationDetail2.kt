package com.adoptvillage.bridge.fragment.applicationListFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.ApplicationsListActivity
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.fragment.applicationFormFragment.APPLICATIONFRAGTAG
import com.adoptvillage.bridge.models.applicationModels.AcceptApplicationDefaultResponse
import com.adoptvillage.bridge.models.applicationModels.AcceptApplicationModel
import com.adoptvillage.bridge.service.RetrofitClient
import kotlinx.android.synthetic.main.fragment_application_detail_2.*
import kotlinx.android.synthetic.main.fragment_applications_list.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ApplicationDetail2 : Fragment() {

    private var isFullAmountDonating=false
    var isSelected = false

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
        return inflater.inflate(R.layout.fragment_application_detail_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ApplicationsListActivity.fragnumber=2
        tvAppDetail2BackSetOnClickListener()
        btnAppDetail2AcceptSetOnClickListener()
        btnAmountSwitchSetOnClickListener()
        tvDonateFullAmountSetOnClickListener()
    }

    private fun tvDonateFullAmountSetOnClickListener() {

        tvDonateFullAmount.setOnClickListener {
            btnAmountSwitch.isChecked = !btnAmountSwitch.isChecked
            if(btnAmountSwitch.isChecked)
            {
                isFullAmountDonating=true
                cvDonationAmount.visibility = View.GONE
            }
            else
            {
                isFullAmountDonating=false
                cvDonationAmount.visibility = View.VISIBLE
            }
        }
    }

    private fun toastMaker(message: String?) {
        if (ApplicationsListActivity.fragnumber==2) {
            Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun btnAmountSwitchSetOnClickListener()
    {
        btnAmountSwitch.setOnCheckedChangeListener{ buttonView, isChecked->
            if(isChecked)
            {
                isFullAmountDonating=true
                cvDonationAmount.visibility = View.GONE
            }
            else
            {
                isFullAmountDonating=false
                cvDonationAmount.visibility = View.VISIBLE
            }
        }
    }

    private fun btnAppDetail2AcceptSetOnClickListener() {
        btnAppDetail2Accept.setOnClickListener {
            if (ApplicationsListActivity.applicationList[ApplicationsListActivity.selectedApplicationNumber].remainingAmount!=null) {
                if (etDonationAmount.text.toString()
                        .toInt() > ApplicationsListActivity.applicationList[ApplicationsListActivity.selectedApplicationNumber].remainingAmount!! && !isFullAmountDonating
                ){
                    toastMaker("Cannot donate more than "+ApplicationsListActivity.applicationList[ApplicationsListActivity.selectedApplicationNumber].remainingAmount!!.toString())
                }else {
                    pbAppDetail2.visibility = View.VISIBLE
                    val moderatorEmail = etModeratorEmailAddress.text.toString()
                    var amount = ApplicationsListActivity.applicationList[ApplicationsListActivity.selectedApplicationNumber].remainingAmount!!
                    if (!isFullAmountDonating) {
                        amount = etDonationAmount.text.toString().toInt()
                    }
                    val obj = AcceptApplicationModel(
                        donatingFullAmount = isFullAmountDonating,
                        amount = amount,
                        moderatorEmail = moderatorEmail,
                        applicationId = ApplicationsListActivity.applicationList[ApplicationsListActivity.selectedApplicationNumber].id
                    )
                    RetrofitClient.instance.applicationService.acceptApplication(obj)
                        .enqueue(object : Callback<AcceptApplicationDefaultResponse> {
                            override fun onResponse(
                                call: Call<AcceptApplicationDefaultResponse>,
                                response: Response<AcceptApplicationDefaultResponse>
                            ) {
                                if (response.isSuccessful) {
                                    if (ApplicationsListActivity.fragnumber == 0) {
                                        if (pbAppList != null) {
                                            pbAppDetail2?.visibility = View.INVISIBLE
                                        }
                                    }
                                    toastMaker(response.body()?.message)
                                    val intent = Intent(context, DashboardActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)
                                } else {
                                    val jObjError = JSONObject(response.errorBody()!!.string())
                                    Log.i(APPLICATIONFRAGTAG, response.toString())
                                    Log.i(APPLICATIONFRAGTAG, jObjError.getString("message"))
                                    toastMaker("Failed to Accept - " + jObjError.getString("message"))
                                    if (ApplicationsListActivity.fragnumber == 0) {
                                        if (pbAppList != null) {
                                            pbAppDetail2?.visibility = View.INVISIBLE
                                        }
                                    }
                                }
                            }

                            override fun onFailure(
                                call: Call<AcceptApplicationDefaultResponse>,
                                t: Throwable
                            ) {
                                Log.i(APPLICATIONFRAGTAG, "error" + t.message)
                                toastMaker("No Internet / Server Down")
                                if (ApplicationsListActivity.fragnumber == 0) {
                                    if (pbAppList != null) {
                                        pbAppDetail2?.visibility = View.INVISIBLE
                                    }
                                }
                            }
                        })
                }
            }
        }
    }

    private fun tvAppDetail2BackSetOnClickListener() {
        tvAppDetail2Back.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fl_wrapper_applications, ApplicationDetail1())?.commit()
        }
    }
 
}


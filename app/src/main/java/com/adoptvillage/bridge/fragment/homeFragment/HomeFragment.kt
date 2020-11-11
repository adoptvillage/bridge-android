package com.adoptvillage.bridge.fragment.homeFragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.adapters.CardAdapter
import com.adoptvillage.bridge.models.cardModels.CardModel
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.*
import com.adoptvillage.bridge.models.DashboardDefaultResponse
import com.adoptvillage.bridge.models.profileModels.GetPrefLocationDefaultResponse
import com.adoptvillage.bridge.service.RetrofitClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(),OnCardClicked {

    private var isCardInfoDisplayed: Boolean=false
    private val HOMEFRAGTAG="HOMEFRAGTAG"
    private lateinit var cardModelList: ArrayList<CardModel>
    private lateinit var cardAdapter: CardAdapter
    private lateinit var prefs: SharedPreferences
    private lateinit var mAuth: FirebaseAuth
    private lateinit var idTokenn:String

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs=context!!.getSharedPreferences(
            activity?.getString(R.string.parent_package_name),
            Context.MODE_PRIVATE
        )
        mAuth= FirebaseAuth.getInstance()
        DashboardActivity.fragmentNumberSaver=1
        btnSubmitApplicationSetOnClickListener()
        btnOnlyForDonor()
        getSavedDashboardCards()
        btnApplicationsSetOnClickListener()
        getIDToken()

        when (DashboardActivity.role) {
            1 -> {
                displaySavedPrefLocation()
            }
            2 -> {
                tvHFVillageAdopted.text="Fetching    00"
            }
            3 -> {
                tvHFVillageAdopted.text="00"
            }
        }
    }
    private fun loadCards()
    {
        cardModelList = ArrayList()

        cardModelList.add(CardModel("......","Fetching...","00","......",0))
        if (DashboardActivity.fragmentNumberSaver==1) {
            val adapter=context?.let { CardAdapter(it, cardModelList, this) }
            if(adapter!=null) {
                cardAdapter = adapter
                if (DashboardActivity.fragmentNumberSaver==1) {
                    slideView?.adapter = cardAdapter
                    slideView?.setPadding(20, 10, 20, 10)
                }
            }
        }

    }

    private fun getSavedDashboardCards() {
        if (DashboardActivity.isDashboardAPIResponseInitialised){
            updateDashboardCards()
        }
        else{
            loadCards()
        }
    }

    private fun getDashboardInfo() {

        RetrofitClient.instance.dashboardService.getUserRole()
            .enqueue(object : Callback<DashboardDefaultResponse> {
                override fun onResponse(
                    call: Call<DashboardDefaultResponse>,
                    response: Response<DashboardDefaultResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!=null) {
                            DashboardActivity.dashboardAPIResponse = response.body()!!
                            DashboardActivity.isDashboardAPIResponseInitialised=true
                            updateDashboardCards()
                            when (DashboardActivity.role) {
                                2 -> {
                                    if (DashboardActivity.dashboardAPIResponse.applications?.isNotEmpty()!!) {
                                        val currentApplication=DashboardActivity.dashboardAPIResponse.applications?.get(0)?.institute + " - " + DashboardActivity.dashboardAPIResponse.applications?.get(0)?.donatingAmount.toString()
                                        if (DashboardActivity.fragmentNumberSaver==1) {
                                            tvHFVillageAdopted?.text = currentApplication
                                        }
                                    }
                                    else{
                                        val currentApplication="No Records    00"
                                        if (DashboardActivity.fragmentNumberSaver==1) {
                                            tvHFVillageAdopted?.text = currentApplication
                                        }
                                    }
                                }
                                3 -> {
                                    if (DashboardActivity.dashboardAPIResponse.applications!!.size>=0) {
                                        if (DashboardActivity.fragmentNumberSaver==1) {
                                            tvHFVillageAdopted?.text =
                                                DashboardActivity.dashboardAPIResponse.applications!!.size.toString()
                                        }
                                    }
                                    else{
                                        if (DashboardActivity.fragmentNumberSaver==1) {
                                            tvHFVillageAdopted?.text = "00"
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            toastMaker("No Internet / Server Down")
                        }

                    } else {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Log.i(HOMEFRAGTAG, response.toString())
                        Log.i(HOMEFRAGTAG, jObjError.getString("message"))
                        toastMaker("Server Error"+jObjError.getString("message"))

                    }
                }

                override fun onFailure(call: Call<DashboardDefaultResponse>, t: Throwable) {
                    Log.i(HOMEFRAGTAG, "error" + t.message)
                    toastMaker("No Internet / Server Down")
                }
            })
    }

    private fun updateDashboardCards() {
        if (DashboardActivity.dashboardAPIResponse!=null){
            if(DashboardActivity.fragmentNumberSaver==1){
                cardModelList = ArrayList()
                if (DashboardActivity.dashboardAPIResponse.applications!=null) {
                    for (i in DashboardActivity.dashboardAPIResponse.applications!!.indices) {

                        val recipientName =
                            DashboardActivity.dashboardAPIResponse.applications!![i]?.applicantFirstName + " " + DashboardActivity.dashboardAPIResponse.applications!![i]?.applicantLastName
                        val amount= DashboardActivity.dashboardAPIResponse.applications!![i]?.donatingAmount
                        val donorName= DashboardActivity.dashboardAPIResponse.applications!![i]?.donorName!!
                        val moderatorName=DashboardActivity.dashboardAPIResponse.applications!![i]?.moderatorName!!
                        var status=DashboardActivity.dashboardAPIResponse.applications!![i]?.status
                        if (status==null){
                            status=0
                        }
                        cardModelList.add(CardModel(donorName,recipientName,amount.toString(),moderatorName,status))
                    }
                    if (cardModelList.isNotEmpty() && DashboardActivity.fragmentNumberSaver==1) {
                        clWelcomeBox?.visibility = View.GONE
                        slideView?.visibility = View.VISIBLE
                        if(DashboardActivity.role == 2)
                        {
                            if (DashboardActivity.fragmentNumberSaver==1) {
                                successCard?.visibility = View.VISIBLE
                            }
                        }
                        val adapter=context?.let { CardAdapter(it, cardModelList, this) }
                        if(adapter!=null) {
                            cardAdapter = adapter
                            if (DashboardActivity.fragmentNumberSaver==1) {
                                slideView?.adapter = cardAdapter
                                slideView?.setPadding(20, 10, 20, 10)
                                isCardInfoDisplayed=true
                            }
                        }
                    }else{
/*
* Role 1 -> DONOR
* Role 2 -> RECIPIENT
* Role 3 -> MODERATOR
* */
                        if (DashboardActivity.fragmentNumberSaver==1) {
                            slideView?.visibility = View.INVISIBLE
                            clWelcomeBox?.visibility = View.VISIBLE
                        }
                        when(DashboardActivity.role)
                        {
                            1 ->
                            {
                                if (DashboardActivity.fragmentNumberSaver==1) {
                                    tvWelcomeMessage?.text = "Start Donating by adopting a village"
                                }
                            }
                            2 ->
                            {
                                if (DashboardActivity.fragmentNumberSaver==1) {
                                    tvWelcomeMessage?.text =
                                        "Submit an application to reach Bridge Donors"
                                    successCard?.visibility = View.GONE
                                }
                            }
                            3 -> {
                                if (DashboardActivity.fragmentNumberSaver==1) {
                                    tvWelcomeMessage?.text =
                                        "You are not moderating any application currently"
                                }
                            }
                        }
                    }
                }
                else{
                    cardModelList = ArrayList()
                    cardModelList.add(CardModel("......","No records","00","......",0))
                    if (DashboardActivity.fragmentNumberSaver==1) {
                        val adapter=context?.let { CardAdapter(it, cardModelList, this) }
                        if(adapter!=null) {
                            cardAdapter = adapter
                            if (DashboardActivity.fragmentNumberSaver==1) {
                                slideView?.adapter = cardAdapter
                                slideView?.setPadding(20, 10, 20, 10)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun displaySavedPrefLocation() {
        if (prefs.getString(activity?.getString(R.string.state),"null")!="null"){
            DashboardActivity.state=prefs.getString(activity?.getString(R.string.state),"")!!
            DashboardActivity.district=prefs.getString(activity?.getString(R.string.district),"")!!
            DashboardActivity.subDistrict=prefs.getString(activity?.getString(R.string.sub_district),"")!!
            DashboardActivity.village=prefs.getString(activity?.getString(R.string.village),"")!!
            val adoptVillage=DashboardActivity.state+", "+DashboardActivity.district+", "+DashboardActivity.subDistrict+", "+DashboardActivity.village
            if (DashboardActivity.fragmentNumberSaver==1) {
                tvHFVillageAdopted.text = adoptVillage
            }
        }
    }

    private fun getIDToken() {
        if (RetrofitClient.instance.idToken=="" && mAuth.currentUser!=null) {
            idTokenn = ""
            mAuth.currentUser!!.getIdToken(true).addOnCompleteListener {
                if (it.isSuccessful) {
                    idTokenn = it.result!!.token!!
                    RetrofitClient.instance.idToken = idTokenn
                    getDashboardInfo()
                    if (DashboardActivity.role==1) {
                        getPrefLocation()
                    }
                } else {
                    Log.i(HOMEFRAGTAG, it.exception.toString())
                    toastMaker("No Internet / Server Down")
                }
            }.addOnFailureListener {
                Log.i(HOMEFRAGTAG, it.message)
                toastMaker("No Internet / Server Down")
            }
        }
        else if (mAuth.currentUser==null){
            toastMaker("Unable to connect - Login again")
            logout()
        } else if (RetrofitClient.instance.idToken!=""){
            getDashboardInfo()
            if (DashboardActivity.role==1) {
                getPrefLocation()
            }
        }
    }
    private fun logout() {
        prefs.edit().putBoolean(activity?.getString(R.string.is_Logged_In), false).apply()
        prefs.edit().putBoolean(activity?.getString(R.string.is_profile_saved), false).apply()
        prefs.edit().putString(activity?.getString(R.string.state),"State").apply()
        prefs.edit().putString(activity?.getString(R.string.district),"District").apply()
        prefs.edit().putString(activity?.getString(R.string.sub_district),"Sub District").apply()
        prefs.edit().putString(activity?.getString(R.string.village),"Village").apply()
        DashboardActivity.fragmentNumberSaver=4
        mAuth.signOut()
        val intent=Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun getPrefLocation() {
        RetrofitClient.instance.dashboardService.getPrefLocation()
            .enqueue(object : Callback<GetPrefLocationDefaultResponse> {
                override fun onResponse(
                    call: Call<GetPrefLocationDefaultResponse>,
                    response: Response<GetPrefLocationDefaultResponse>
                ) {
                    if (response.isSuccessful) {
                        DashboardActivity.state=response.body()?.state!!
                        DashboardActivity.district=response.body()?.district!!
                        DashboardActivity.subDistrict=response.body()?.subDistrict!!
                        DashboardActivity.village=response.body()?.area!!
                        savePrefLocation()
                        val adoptVillage=DashboardActivity.state+", "+DashboardActivity.district+", "+DashboardActivity.subDistrict+", "+DashboardActivity.village
                        if (DashboardActivity.fragmentNumberSaver==1) {
                            if (tvHFVillageAdopted!=null) {
                                tvHFVillageAdopted?.text = adoptVillage
                            }
                        }
                    } else {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Log.i(HOMEFRAGTAG, response.toString())
                        Log.i(HOMEFRAGTAG, jObjError.getString("message"))
                        toastMaker("Failed to fetch Adopted Village - "+jObjError.getString("message"))
                    }
                }

                override fun onFailure(call: Call<GetPrefLocationDefaultResponse>, t: Throwable) {
                    Log.i(HOMEFRAGTAG, "error" + t.message)
                    toastMaker("No Internet / Server Down")
                }
            })
    }

    private fun savePrefLocation() {
        prefs.edit().putString(activity?.getString(R.string.state),DashboardActivity.state).apply()
        prefs.edit().putString(activity?.getString(R.string.district),DashboardActivity.district).apply()
        prefs.edit().putString(activity?.getString(R.string.sub_district),DashboardActivity.subDistrict).apply()
        prefs.edit().putString(activity?.getString(R.string.village),DashboardActivity.village).apply()
    }

    private fun toastMaker(message: String?) {
        if (DashboardActivity.fragmentNumberSaver==1) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun btnSubmitApplicationSetOnClickListener() {
        btnSubmitApplication.setOnClickListener {
            val intent= Intent(context, ApplicationFormActivity::class.java)
            startActivity(intent)
        }
    }

    private fun btnApplicationsSetOnClickListener() {
        btnApplications.setOnClickListener{
            val intent = Intent(context, ApplicationsListActivity::class.java)
            startActivity(intent)
        }
    }

/*
* Role 1 -> DONOR
* Role 2 -> RECIPIENT
* Role 3 -> MODERATOR
* */

    private fun btnOnlyForDonor() {
        when (DashboardActivity.role) {
            1 -> {
                btnApplications.visibility=View.VISIBLE
                successCard.visibility=View.VISIBLE
                btnSubmitApplication.visibility=View.INVISIBLE
                textView12.text=activity?.getString(R.string.village_adopted)
            }
            2 -> {
                btnApplications.visibility=View.INVISIBLE
                successCard.visibility=View.VISIBLE
                btnSubmitApplication.visibility=View.VISIBLE
                textView12.text=getString(R.string.current_application)
            }
            3 -> {
                btnApplications.visibility=View.INVISIBLE
                successCard.visibility=View.VISIBLE
                btnSubmitApplication.visibility=View.INVISIBLE
                textView12.text=getString(R.string.application_to_review)
            }
        }
    }

    override fun onCardClicked(position: Int) {
        if (isCardInfoDisplayed) {
            DashboardActivity.fragmentNumberSaver = 5
            DashboardActivity.cardPositionClicked=position
            val intent = Intent(context, ChatActivity::class.java)
            startActivity(intent)
        }
    }
}

interface OnCardClicked
{
    fun onCardClicked(position: Int)
}


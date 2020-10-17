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
import com.adoptvillage.bridge.activity.ApplicationFormActivity
import com.adoptvillage.bridge.activity.ApplicationsListActivity
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.activity.MainActivity
import com.adoptvillage.bridge.models.profileModels.GetPrefLoactionDefaultResponse
import com.adoptvillage.bridge.service.RetrofitClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private val HOMEFRAGTAG="HOMEFRAGTAG"
    private lateinit var cardModelList: ArrayList<CardModel>
    private lateinit var cardAdapter: CardAdapter
    private lateinit var prefs: SharedPreferences
    lateinit var mAuth: FirebaseAuth
    lateinit var idTokenn:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.explode)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loadCards()
    }

    private fun loadCards()
    {
        cardModelList = ArrayList()

        cardModelList.add(CardModel("Abhi","Vatsal","300","Mr. A"))
        cardModelList.add(CardModel("Ankit","Abhishek","500","Mr. B"))
        cardModelList.add(CardModel("Raju","Rasmeet","1000","Mr. C"))
        cardModelList.add(CardModel("Humraz","John Doe","400","Mr. C"))
        cardAdapter = activity?.let { CardAdapter(it,cardModelList) }!!
        slideView.adapter = cardAdapter
        slideView.setPadding(20,10,20,10)

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
        btnApplicationsSetOnClickListener()
        getIDToken()
        if (DashboardActivity.role==1) {
            displaySavedPrefLocation()
        }else if (DashboardActivity.role==2){
            tvHFVillageAdopted.text="Chandigarh University    80000"
        }else if (DashboardActivity.role==3){
            tvHFVillageAdopted.text="3"
        }
    }

    private fun displaySavedPrefLocation() {
        if (prefs.getString(activity?.getString(R.string.state),"null")!="null"){
            DashboardActivity.state=prefs.getString(activity?.getString(R.string.state),"")!!
            DashboardActivity.district=prefs.getString(activity?.getString(R.string.district),"")!!
            DashboardActivity.subDistrict=prefs.getString(activity?.getString(R.string.sub_district),"")!!
            DashboardActivity.village=prefs.getString(activity?.getString(R.string.village),"")!!
            val adoptVillage=DashboardActivity.state+", "+DashboardActivity.district+", "+DashboardActivity.subDistrict+", "+DashboardActivity.village
            tvHFVillageAdopted.text=adoptVillage
        }
    }

    private fun getIDToken() {
        if (RetrofitClient.instance.idToken=="" && mAuth.currentUser!=null) {
            idTokenn = ""
            mAuth.currentUser!!.getIdToken(true).addOnCompleteListener {
                if (it.isSuccessful) {
                    idTokenn = it.result!!.token!!
                    RetrofitClient.instance.idToken = idTokenn
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
            if (DashboardActivity.role==1) {
                getPrefLocation()
            }
        }
    }
    private fun logout() {
        prefs.edit().putBoolean(activity?.getString(R.string.is_Logged_In), false).apply()
        prefs.edit().putBoolean(activity?.getString(R.string.is_profile_saved), false).apply()
        DashboardActivity.fragmentNumberSaver=4
        mAuth.signOut()
        val intent=Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun getPrefLocation() {
        RetrofitClient.instance.dashboardService.getPrefLocation()
            .enqueue(object : Callback<GetPrefLoactionDefaultResponse> {
                override fun onResponse(
                    call: Call<GetPrefLoactionDefaultResponse>,
                    response: Response<GetPrefLoactionDefaultResponse>
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

                override fun onFailure(call: Call<GetPrefLoactionDefaultResponse>, t: Throwable) {
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
}


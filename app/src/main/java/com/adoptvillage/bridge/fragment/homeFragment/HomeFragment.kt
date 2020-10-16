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
import com.adoptvillage.bridge.models.CardModel
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.ApplicationFormActivity
import com.adoptvillage.bridge.activity.ApplicationsListActivity
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.models.GetPrefLoactionDefaultResponse
import com.adoptvillage.bridge.service.RetrofitClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
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
        getIDToken()
        btnAdoptVillageSetOnClickListener()
        btnSubmitApplicationSetOnClickListener()
        btnOnlyForDonor()
        btnApplicationsSetOnClickListener()
    }
    private fun getIDToken() {
        idTokenn = ""
        mAuth.currentUser!!.getIdToken(true).addOnCompleteListener {
            if (it.isSuccessful) {
                idTokenn = it.result!!.token!!
                RetrofitClient.instance.idToken=idTokenn
                if (prefs.getInt(activity?.getString(R.string.role), 0) == 1) {
                    getPrefLocation()
                }
            }else{
                toastMaker("Error while fetching IdToken")
            }
        }
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
                    } else {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Log.i(HOMEFRAGTAG, response.toString())
                        Log.i(HOMEFRAGTAG, jObjError.getString("message"))
                        toastMaker(jObjError.getString("message"))
                    }
                }

                override fun onFailure(call: Call<GetPrefLoactionDefaultResponse>, t: Throwable) {
                    Log.i(HOMEFRAGTAG, "error" + t.message)
                    toastMaker("Failed To Fetch Profile - " + t.message)
                }
            })
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

    private fun btnApplicationsSetOnClickListener()
    {
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
        when {
            prefs.getInt(activity?.getString(R.string.role), 0) == 1 -> {
                btnApplications.visibility=View.VISIBLE
                btnAdoptVillage.visibility=View.VISIBLE
                btnSubmitApplication.visibility=View.INVISIBLE
            }
            prefs.getInt(activity?.getString(R.string.role), 0) == 2 -> {
                btnApplications.visibility=View.INVISIBLE
                btnAdoptVillage.visibility=View.INVISIBLE
                btnSubmitApplication.visibility=View.VISIBLE
                tvAmountStatus.text = "Amount\nReceived"
            }
            prefs.getInt(activity?.getString(R.string.role), 0) == 3 -> {
                btnApplications.visibility=View.INVISIBLE
                btnAdoptVillage.visibility=View.INVISIBLE
                btnSubmitApplication.visibility=View.INVISIBLE
                tvAmountStatus.text = "Amount\nProcessed"
            }
        }
    }

    private fun btnAdoptVillageSetOnClickListener() {
        btnAdoptVillage.setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fl_wrapper, LocationFragment())?.addToBackStack(javaClass.name)?.commit()
        }
    }
}


package com.adoptvillage.bridge.fragment.profileFragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.activity.MainActivity
import com.adoptvillage.bridge.models.ProfileDefaultResponse
import com.adoptvillage.bridge.models.UpdateProfileDefaultResponse
import com.adoptvillage.bridge.models.UpdateProfileModel
import com.adoptvillage.bridge.service.RetrofitClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private var PROFILEFRAGTAG="PROFILEFRAGTAG"
class ProfileFragment : Fragment() {

    private var isInProfileEditMode=false
    private lateinit var prefs: SharedPreferences
    lateinit var mAuth:FirebaseAuth
    lateinit var idTokenn:String

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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs=context!!.getSharedPreferences(
            activity?.getString(R.string.parent_package_name),
            Context.MODE_PRIVATE
        )
        btnLogoutSetOnClickListener()
        btnPSEditSetOnClickListener()
        displaySavedProfile()
        mAuth= FirebaseAuth.getInstance()
        getIDToken()

    }

    private fun getIDToken() {
        mAuth.currentUser!!.getIdToken(true).addOnCompleteListener {
            if (it.isSuccessful) {
                idTokenn = it.result!!.token!!
                callingAfterGettingIdToken()
            }
            else{
                Log.i(PROFILEFRAGTAG,it.exception.toString())
                toastMaker("Error while fetching Profile")
                pbPSProfileFetch?.visibility = View.INVISIBLE
            }
        }
    }

    private fun callingAfterGettingIdToken() {
        Log.i(PROFILEFRAGTAG,idTokenn)
        RetrofitClient.instance.idToken=idTokenn
        getProfile()

    }

    private fun btnPSEditSetOnClickListener() {
        btnPSEdit.setOnClickListener {
            if (isInProfileEditMode) {
                btnPSEdit.text = activity?.getString(R.string.edit)
                isInProfileEditMode=false
                etPSName.isEnabled=false
                etPSAddress.isEnabled=false
                etPSOccupation.isEnabled=false
                etPSCountry.isEnabled=false
                etPSEmail.setTextColor(Color.BLACK)
                etPSRole.setTextColor(Color.BLACK)
                updateEditedProfile()
            }
            else{
                btnPSEdit.text = activity?.getString(R.string.save)
                isInProfileEditMode=true
                etPSName.isEnabled=true
                etPSAddress.isEnabled=true
                etPSCountry.isEnabled=true
                etPSOccupation.isEnabled=true
                etPSEmail.setTextColor(Color.GRAY)
                etPSRole.setTextColor(Color.GRAY)
            }
        }
    }

    private fun updateEditedProfile() {
        val name=etPSName.text.toString().trim()
        val address=etPSAddress.text.toString().trim()
        val location=etPSCountry.text.toString().trim()
        val occupation=etPSOccupation.text.toString().trim()
        val obj=UpdateProfileModel(name, address, location, occupation)
        RetrofitClient.instance.profileService.updateProfile(obj)
            .enqueue(object : Callback<UpdateProfileDefaultResponse> {
                override fun onResponse(
                    call: Call<UpdateProfileDefaultResponse>,
                    response: Response<UpdateProfileDefaultResponse>
                ) {
                    if (response.isSuccessful) {
                        toastMaker(response.body()?.message)
                        getProfile()
                    } else {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Log.i(PROFILEFRAGTAG, response.toString())
                        Log.i(PROFILEFRAGTAG, jObjError.getString("message"))
                        toastMaker(jObjError.getString("message"))
                    }
                }

                override fun onFailure(call: Call<UpdateProfileDefaultResponse>, t: Throwable) {
                    Log.i(PROFILEFRAGTAG, "error" + t.message)
                    toastMaker("Failed To Fetch Profile - " + t.message)
                    pbPSProfileFetch?.visibility = View.INVISIBLE
                }

            })
    }

    private fun toastMaker(message: String?) {
        if(DashboardActivity.fragmentNumberSaver==0){
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun displaySavedProfile() {
        if(prefs.getBoolean(activity?.getString(R.string.is_profile_saved), false)) {
            pbPSProfileFetch?.visibility=View.INVISIBLE
            val name = prefs.getString(activity?.getString(R.string.name), "")
            val address = prefs.getString(activity?.getString(R.string.address), "")
            val location = prefs.getString(activity?.getString(R.string.location), "")
            val email = prefs.getString(activity?.getString(R.string.email), "")
            val occupation = prefs.getString(activity?.getString(R.string.occupation), "")
            var role:String? = ""
            when {
                prefs.getInt(activity?.getString(R.string.role), 0) == 1 -> {
                    role = activity?.getString(R.string.donor)
                }
                prefs.getInt(activity?.getString(R.string.role), 0) == 2 -> {
                    role = activity?.getString(R.string.recipient)
                }
                prefs.getInt(activity?.getString(R.string.role), 0) == 3 -> {
                    role = activity?.getString(R.string.moderator)
                }
            }
            etPSAddress.setText(address)
            etPSCountry.setText(location)
            etPSEmail.setText(email)
            etPSName.setText(name)
            etPSOccupation.setText(occupation)
            etPSRole.setText(role)
        }
        else{
            pbPSProfileFetch?.visibility=View.VISIBLE
        }
    }

    private fun getProfile() {
        RetrofitClient.instance.profileService.getUserProfile()
            .enqueue(object : Callback<ProfileDefaultResponse> {
                override fun onResponse(
                    call: Call<ProfileDefaultResponse>,
                    response: Response<ProfileDefaultResponse>
                ) {
                    if (response.isSuccessful) {
                        updateProfile(response)
                        saveProfileDetail(response)
                    } else {
                        pbPSProfileFetch?.visibility = View.INVISIBLE
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Log.i(PROFILEFRAGTAG, response.toString())
                        Log.i(PROFILEFRAGTAG, jObjError.getString("message"))
                        toastMaker(jObjError.getString("message"))
                    }
                }

                override fun onFailure(call: Call<ProfileDefaultResponse>, t: Throwable) {
                    pbPSProfileFetch?.visibility = View.INVISIBLE
                    Log.i(PROFILEFRAGTAG, "error" + t.message)
                    toastMaker("Failed To Fetch Profile - " + t.message)
                    pbPSProfileFetch?.visibility = View.INVISIBLE
                }
            })
    }

    private fun saveProfileDetail(response: Response<ProfileDefaultResponse>) {
            prefs.edit().putString(activity?.getString(R.string.name), response.body()?.name).apply()
            prefs.edit().putString(activity?.getString(R.string.address), response.body()?.address)
                .apply()
            prefs.edit().putString(activity?.getString(R.string.email), response.body()?.email).apply()
            prefs.edit().putString(
                activity?.getString(R.string.location),
                response.body()?.location
            )
                .apply()
            prefs.edit().putString(
                activity?.getString(R.string.occupation),
                response.body()?.occupation
            )
                .apply()
            prefs.edit().putBoolean(
                activity?.getString(R.string.is_email_verified),
                response.body()?.isEmailVerified!!
            ).apply()
            prefs.edit().putBoolean(activity?.getString(R.string.is_profile_saved), true).apply()
            when {
                response.body()?.isDonor == true -> {
                    prefs.edit().putInt(activity?.getString(R.string.role), 1).apply()
                }
                response.body()?.isRecipient == true -> {
                    prefs.edit().putInt(activity?.getString(R.string.role), 2).apply()
                }
                response.body()?.isModerator == true -> {
                    prefs.edit().putInt(activity?.getString(R.string.role), 3).apply()
                }
            }
        
    }

    private fun updateProfile(response: Response<ProfileDefaultResponse>) {
        pbPSProfileFetch?.visibility = View.INVISIBLE
        etPSAddress?.setText(response.body()?.address)
        etPSCountry?.setText(response.body()?.location)
        etPSEmail?.setText(response.body()?.email)
        etPSName?.setText(response.body()?.name)
        etPSOccupation?.setText(response.body()?.occupation)
        when {
            response.body()?.isDonor==true -> {
                etPSRole?.setText(R.string.donor)
            }
            response.body()?.isRecipient==true -> {
                etPSRole?.setText(R.string.recipient)
            }
            response.body()?.isModerator==true -> {
                etPSRole?.setText(R.string.moderator)
            }
        }
    }

    private fun btnLogoutSetOnClickListener() {
        btnPSLogout.setOnClickListener {
            prefs.edit().putBoolean(activity?.getString(R.string.is_Logged_In), false).apply()
            prefs.edit().putBoolean(activity?.getString(R.string.is_profile_saved), false).apply()
            DashboardActivity.fragmentNumberSaver=4
            val intent=Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }


}



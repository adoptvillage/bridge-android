package com.adoptvillage.bridge.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.MainActivity
import com.adoptvillage.bridge.models.ProfileDefaultResponse
import com.adoptvillage.bridge.service.RetrofitClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private var PROFILEFRAGTAG="PROFILEFRAGTAG"
class ProfileFragment : Fragment() {

    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            getString(R.string.parent_package_name),
            Context.MODE_PRIVATE
        )
        val idToken=prefs.getString(getString(R.string.idToken), "")
        if (idToken != null) {
            RetrofitClient.instance.idToken=idToken
        }
        displaySavedProfile()
        getProfile()
        btnLogoutSetOnClickListener()
    }

    private fun displaySavedProfile() {
        if(prefs.getBoolean(getString(R.string.is_profile_saved), false)) {
            pbPSProfileFetch.visibility=View.INVISIBLE
            val name = prefs.getString(getString(R.string.name), "")
            val address = prefs.getString(getString(R.string.address), "")
            val location = prefs.getString(getString(R.string.location), "")
            val email = prefs.getString(getString(R.string.email), "")
            val occupation = prefs.getString(getString(R.string.occupation), "")
            var role = ""
            when {
                prefs.getString(getString(R.string.role), "") == "1" -> {
                    role = getString(R.string.donor)
                }
                prefs.getString(getString(R.string.role), "") == "2" -> {
                    role = getString(R.string.recipient)
                }
                prefs.getString(getString(R.string.role), "") == "3" -> {
                    role = getString(R.string.moderator)
                }
            }
            etPSAddress.setText(address)
            etPSCity.setText(location)
            etPSEmail.setText(email)
            etPSName.setText(name)
            etPSOccupation.setText(occupation)
            etPSRole.setText(role)
        }
        else{
            pbPSProfileFetch.visibility=View.VISIBLE
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
                        pbPSProfileFetch.visibility = View.INVISIBLE
                        updateProfile(response)
                        saveProfileDetail(response)
                    } else {
                        pbPSProfileFetch.visibility = View.INVISIBLE
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Log.i(PROFILEFRAGTAG, response.toString())
                        Log.i(PROFILEFRAGTAG, jObjError.getString("message"))
                        Snackbar.make(
                            clPSMAinScreen,
                            jObjError.getString("message"),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ProfileDefaultResponse>, t: Throwable) {
                    pbPSProfileFetch.visibility = View.INVISIBLE
                    Log.i(PROFILEFRAGTAG, t.message)
                    Snackbar.make(
                        clPSMAinScreen,
                        "Failed To Fetch Profile - " + t.message,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun saveProfileDetail(response: Response<ProfileDefaultResponse>) {
        prefs.edit().putString(getString(R.string.name), response.body()?.name).apply()
        prefs.edit().putString(getString(R.string.address), response.body()?.address).apply()
        prefs.edit().putString(getString(R.string.email), response.body()?.email).apply()
        prefs.edit().putString(getString(R.string.location), response.body()?.location).apply()
        prefs.edit().putString(getString(R.string.occupation), response.body()?.occupation).apply()
        prefs.edit().putBoolean(
            getString(R.string.is_email_verified),
            response.body()?.isEmailVerified!!
        ).apply()
        prefs.edit().putBoolean(getString(R.string.is_profile_saved), true).apply()
    }

    private fun updateProfile(response: Response<ProfileDefaultResponse>) {
        etPSAddress.setText(response.body()?.address)
        etPSCity.setText(response.body()?.location)
        etPSEmail.setText(response.body()?.email)
        etPSName.setText(response.body()?.name)
        etPSOccupation.setText(response.body()?.occupation)
        var role=""
        when {
            prefs.getString(getString(R.string.role), "") == "1" -> {
                role = getString(R.string.donor)
            }
            prefs.getString(getString(R.string.role), "") == "2" -> {
                role = getString(R.string.recipient)
            }
            prefs.getString(getString(R.string.role), "") == "3" -> {
                role = getString(R.string.moderator)
            }
        }
        etPSRole.setText(role)
    }

    private fun btnLogoutSetOnClickListener() {
        btnPSLogout.setOnClickListener {
            prefs.edit().putBoolean(getString(R.string.is_Logged_In), false).apply()
            prefs.edit().putBoolean(getString(R.string.is_profile_saved), false).apply()
            startActivity(Intent(context, MainActivity::class.java))
        }
    }

}

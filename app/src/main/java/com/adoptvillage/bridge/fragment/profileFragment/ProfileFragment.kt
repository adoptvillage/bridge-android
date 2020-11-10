package com.adoptvillage.bridge.fragment.profileFragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.adoptvillage.bridge.models.profileModels.GetPrefLoactionDefaultResponse
import com.adoptvillage.bridge.models.profileModels.ProfileDefaultResponse
import com.adoptvillage.bridge.models.profileModels.UpdateProfileDefaultResponse
import com.adoptvillage.bridge.models.profileModels.UpdateProfileModel
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
    private lateinit var mAuth:FirebaseAuth
    private lateinit var idTokenn:String
    val IMAGE=100

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
        isInProfileEditMode=false
        DashboardActivity.fragmentNumberSaver=0
        btnLogoutSetOnClickListener()
        btnPSEditSetOnClickListener()
        displaySavedProfile()
        mAuth= FirebaseAuth.getInstance()
        getIDToken()
        tvPSAdoptVillageButtonSetOnClickListener()
        btnOnlyForDonor()
        civPSProfilePhotoSetOnClickListener()
    }

    private fun civPSProfilePhotoSetOnClickListener()
    {

        civPSProfilePhoto.setOnClickListener {

            if(isInProfileEditMode)
            {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Image"),IMAGE)
            }
            else
            {
                toastMaker("Click on Edit button above to add photo")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == IMAGE)
            {
                if(data!=null)
                {
                    val selectedPhotoUri: Uri = data.data!!
                    civPSProfilePhoto.setImageURI(selectedPhotoUri)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun btnOnlyForDonor() {
        when {
            prefs.getInt(activity?.getString(R.string.role), 0) == 1 -> {
                tvPSAdoptVillageButton.isEnabled=true
            }
            prefs.getInt(activity?.getString(R.string.role), 0) == 2 -> {
                tvPSAdoptVillageButton.text="NA"
                tvPSAdoptVillageButton.isEnabled=false
            }
            prefs.getInt(activity?.getString(R.string.role), 0) == 3 -> {
                tvPSAdoptVillageButton.text="NA"
                tvPSAdoptVillageButton.isEnabled=false
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
                        savePrefLocation()
                        val adoptVillage=DashboardActivity.state+", "+DashboardActivity.district+", "+DashboardActivity.subDistrict+", "+DashboardActivity.village
                        if (DashboardActivity.fragmentNumberSaver==0) {
                            if (tvPSAdoptVillageButton!=null) {
                                tvPSAdoptVillageButton?.text = adoptVillage
                            }
                        }
                    } else {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Log.i(PROFILEFRAGTAG, response.toString())
                        Log.i(PROFILEFRAGTAG, jObjError.getString("message"))
                        toastMaker("Failed to fetch Adopted Village - "+jObjError.getString("message"))
                    }
                }

                override fun onFailure(call: Call<GetPrefLoactionDefaultResponse>, t: Throwable) {
                    Log.i(PROFILEFRAGTAG, "error" + t.message)
                    toastMaker("No Internet / Server Down")
                }
            })
    }

    private fun tvPSAdoptVillageButtonSetOnClickListener() {
        tvPSAdoptVillageButton.setOnClickListener {
            if (isInProfileEditMode) {
                updateEditedProfile()
                activity?.supportFragmentManager?.popBackStackImmediate()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fl_wrapper, LocationFragment())?.addToBackStack(javaClass.name)
                    ?.commit()
            }
            else{
                toastMaker("Click on Edit to Update")
            }
        }
    }

    private fun getIDToken() {
        idTokenn=""
        mAuth.currentUser!!.getIdToken(true).addOnCompleteListener {
            if (it.isSuccessful) {
                idTokenn = it.result!!.token!!
                RetrofitClient.instance.idToken=idTokenn
                callingAfterGettingIdToken()
            }
            else{
                Log.i(PROFILEFRAGTAG,it.exception.toString())
                toastMaker("No Internet / Server Down")
                pbPSProfileFetch?.visibility = View.INVISIBLE
            }
        }.addOnFailureListener {
            Log.i(PROFILEFRAGTAG,it.message)
            toastMaker("No Internet / Server Down")
            pbPSProfileFetch?.visibility = View.INVISIBLE
        }
    }

    private fun callingAfterGettingIdToken() {
        Log.i(PROFILEFRAGTAG,idTokenn)
        if (idTokenn.isEmpty()){
            toastMaker("Unable to fetch profile - Login again")
            logout()
        }
        getProfile()
        if (DashboardActivity.role == 1) {
            getPrefLocation()
        }
    }

    private fun btnPSEditSetOnClickListener() {
        btnPSEdit.setOnClickListener {
            if (isInProfileEditMode) {
                pbPSEdit.visibility=View.VISIBLE
                btnPSEdit.isEnabled=false
                btnPSEdit.text = activity?.getString(R.string.edit)
                isInProfileEditMode=false
                etPSName.isEnabled=false
                etPSAddress.isEnabled=false
                civPSProfilePhoto.isClickable = false
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
                civPSProfilePhoto.isClickable=true
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
        val obj= UpdateProfileModel(name, address, location, occupation)
        RetrofitClient.instance.profileService.updateProfile(obj)
            .enqueue(object : Callback<UpdateProfileDefaultResponse> {
                override fun onResponse(
                    call: Call<UpdateProfileDefaultResponse>,
                    response: Response<UpdateProfileDefaultResponse>
                ) {
                    if (response.isSuccessful) {
                        toastMaker(response.body()?.message)
                        getProfile()
                        if (DashboardActivity.fragmentNumberSaver==0) {
                            pbPSEdit?.visibility = View.INVISIBLE
                            btnPSEdit?.isEnabled = true
                        }
                    } else {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Log.i(PROFILEFRAGTAG, response.toString())
                        Log.i(PROFILEFRAGTAG, jObjError.getString("message"))
                        toastMaker("Failed to update profile - "+jObjError.getString("message"))
                        if (DashboardActivity.fragmentNumberSaver==0) {
                            pbPSEdit?.visibility = View.INVISIBLE
                            btnPSEdit?.isEnabled = true
                        }
                    }
                }

                override fun onFailure(call: Call<UpdateProfileDefaultResponse>, t: Throwable) {
                    Log.i(PROFILEFRAGTAG, "error" + t.message)
                    toastMaker("No Internet / Server Down")
                    pbPSProfileFetch?.visibility = View.INVISIBLE
                    if (DashboardActivity.fragmentNumberSaver==0) {
                        pbPSEdit?.visibility = View.INVISIBLE
                        btnPSEdit?.isEnabled = true
                    }
                }

            })
    }

    private fun toastMaker(message: String?) {
        if(DashboardActivity.fragmentNumberSaver==0){
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePrefLocation() {
        prefs.edit().putString(activity?.getString(R.string.state),DashboardActivity.state).apply()
        prefs.edit().putString(activity?.getString(R.string.district),DashboardActivity.district).apply()
        prefs.edit().putString(activity?.getString(R.string.sub_district),DashboardActivity.subDistrict).apply()
        prefs.edit().putString(activity?.getString(R.string.village),DashboardActivity.village).apply()
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
            val adoptVillage=DashboardActivity.state+", "+DashboardActivity.district+", "+DashboardActivity.subDistrict+", "+DashboardActivity.village
            tvPSAdoptVillageButton.text=adoptVillage
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
                        toastMaker("Failed to fetch profile - "+jObjError.getString("message"))
                    }
                }

                override fun onFailure(call: Call<ProfileDefaultResponse>, t: Throwable) {
                    pbPSProfileFetch?.visibility = View.INVISIBLE
                    Log.i(PROFILEFRAGTAG, "error" + t.message)
                    toastMaker("No Internet / Server Down")
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
            logout()
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


}



package com.adoptvillage.bridge.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.activity.systemDarkGray
import com.adoptvillage.bridge.activity.systemViolet
import com.adoptvillage.bridge.models.Login
import com.adoptvillage.bridge.models.LoginDefaultResponse
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.service.RetrofitClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.android.synthetic.main.fragment_log_in.clMainScreen
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/* Login Fragment
Contain all actions for logging in
Called from Main Activity
 */

//Tag For LOGCAT
private var LOGINFRAGTAG="LOGINFRAGTAG"

class LogInFragment : Fragment() {
    //variables
    var bolEmail=false
    var bolPassword=false
    lateinit var prefs:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //transition
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.explode)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //shared pref
        prefs=context!!.getSharedPreferences(getString(R.string.parent_package_name),Context.MODE_PRIVATE)


        btnLoginSetOnClickListener()
        btnActionSetOnClickListener()
        btnSignUpSetOnClickListener()
        tvLForgetPasswordSetOnClickListener()
        btnSActionEnableListener()
    }

    //Forget Password Button
    private fun tvLForgetPasswordSetOnClickListener() {
        tvLForgetPassword.setOnClickListener {

        }
    }

    //Handles when to enable Login Button
    private fun btnSActionEnableListener() {
        btnLAction.isEnabled=false
        btnLAction.isActivated=false
        btnLAction.isCheckable=false
        btnLAction.setBackgroundColor(Color.parseColor(systemDarkGray))
        btnLAction.setTextColor(Color.parseColor(systemViolet))

        etLEmail.addTextChangedListener {
            bolEmail = !(it.isNullOrBlank() || it.isNullOrEmpty())
            btnLAction.isEnabled = bolEmail && bolPassword
            btnLAction.isActivated = bolEmail && bolPassword
            if (bolEmail && bolPassword){
                btnLAction.setBackgroundColor(Color.parseColor(systemViolet))
                btnLAction.setTextColor(Color.WHITE)
            }
            else{
                btnLAction.setBackgroundColor(Color.parseColor(systemDarkGray))
                btnLAction.setTextColor(Color.parseColor(systemViolet))
            }
        }

        etLPassword.addTextChangedListener {
            bolPassword = !(it.isNullOrBlank() || it.isNullOrEmpty())
            btnLAction.isEnabled = bolEmail && bolPassword
            btnLAction.isActivated = bolEmail && bolPassword
            if (bolEmail && bolPassword){
                btnLAction.setBackgroundColor(Color.parseColor(systemViolet))
                btnLAction.setTextColor(Color.WHITE)
            }
            else{
                btnLAction.setBackgroundColor(Color.parseColor(systemDarkGray))
                btnLAction.setTextColor(Color.parseColor(systemViolet))
            }
        }
    }

    //sign up button
    private fun btnSignUpSetOnClickListener() {
        btnLSignUp.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_main, SignUpFragment())?.commit()
        }
    }

    //Action Button
    private fun btnActionSetOnClickListener() {
        pbLogin.visibility=View.INVISIBLE
        btnLAction.setOnClickListener {
            if (validation()) {
                pbLogin.visibility=View.VISIBLE
                btnLAction.text=""

                val email = etLEmail.text.toString().trim()
                val password = etLPassword.text.toString().trim()
                val obj = Login(email, password)

                RetrofitClient.instance.authService.loginUser(obj)
                    .enqueue(object : Callback<LoginDefaultResponse> {
                        override fun onResponse(
                            call: Call<LoginDefaultResponse>,
                            response: Response<LoginDefaultResponse>
                        ) {
                            if (response.isSuccessful) {
                                Log.i(LOGINFRAGTAG, response.toString())
                                Log.i(LOGINFRAGTAG, response.body()?.displayName)
                                Log.i(LOGINFRAGTAG, response.body()?.email)
                                Log.i(LOGINFRAGTAG, response.body()?.expiresIn)
                                Log.i(LOGINFRAGTAG, response.body()?.idToken)
                                Log.i(LOGINFRAGTAG, response.body()?.kind)
                                Log.i(LOGINFRAGTAG, response.body()?.localId)
                                Log.i(LOGINFRAGTAG, response.body()?.refreshToken)
                                Log.i(LOGINFRAGTAG, response.body()?.role)
                                saveDataInSharedPref(
                                    response.body()?.displayName,
                                    response.body()?.idToken,
                                    response.body()?.localId,
                                    response.body()?.refreshToken,
                                    response.body()?.role
                                )
                                Snackbar.make(clMainScreen, "Logging In", Snackbar.LENGTH_INDEFINITE).show()
                                startActivity(Intent(context, DashboardActivity::class.java))
                                pbLogin.visibility = View.INVISIBLE
                                btnLAction.text = "LOGIN"
                            } else {
                                val jObjError = JSONObject(response.errorBody()!!.string())
                                Log.i(LOGINFRAGTAG, response.toString())
                                Log.i(LOGINFRAGTAG, jObjError.getString("message"))
                                Snackbar.make(clMainScreen,jObjError.getString("message"),Snackbar.LENGTH_LONG).show()
                                pbLogin.visibility=View.INVISIBLE
                                btnLAction.text="LOGIN"
                            }
                        }

                        override fun onFailure(call: Call<LoginDefaultResponse>, t: Throwable) {
                            Log.i(LOGINFRAGTAG, t.message)
                            Snackbar.make(clMainScreen,"Failed To Login - "+t.message,Snackbar.LENGTH_LONG).show()
                            pbLogin.visibility=View.INVISIBLE
                            btnLAction.text="LOGIN"
                        }
                    })
            }
        }
    }

    private fun saveDataInSharedPref(displayName: String?, idToken: String?, localId: String?, refreshToken: String?, role: String?) {
        prefs.edit().putString(getString(R.string.displayName),displayName).apply()
        prefs.edit().putString(getString(R.string.idToken),idToken).apply()
        prefs.edit().putString(getString(R.string.localId),localId).apply()
        prefs.edit().putString(getString(R.string.refreshToken),refreshToken).apply()
        prefs.edit().putString(getString(R.string.role),role).apply()
        prefs.edit().putBoolean(getString(R.string.is_Logged_In),true).apply()
    }

    //validate the input
    private fun validation(): Boolean {
        return if(etLEmail.text.isNullOrEmpty() || etLEmail.text.isNullOrBlank()){
            Snackbar.make(clMainScreen,"Email cannot be Empty",Snackbar.LENGTH_SHORT).show()
            Log.i(LOGINFRAGTAG,"Email cannot be Empty")
            false
        } else if(etLPassword.text.isNullOrEmpty() || etLPassword.text.isNullOrBlank()) {
            Snackbar.make(clMainScreen,"Password cannot be Empty",Snackbar.LENGTH_SHORT).show()
            Log.i(LOGINFRAGTAG,"Password cannot be Empty")
            false
        } else{
            val temp=etLPassword.text.toString().trim()
            if(temp.length<6){
                Snackbar.make(clMainScreen,"Password should be greater than 5",Snackbar.LENGTH_SHORT).show()
                Log.i(LOGINFRAGTAG,"Password should be greater than 5")
                false
            }
            else{
                true
            }
        }
    }

    private fun btnLoginSetOnClickListener() {}
}


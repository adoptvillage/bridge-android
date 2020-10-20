package com.adoptvillage.bridge.fragment.authFragment


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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.activity.systemDarkGray
import com.adoptvillage.bridge.activity.systemViolet
import com.adoptvillage.bridge.models.DashboardDefaultResponse
import com.adoptvillage.bridge.service.RetrofitClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_log_in.*
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
    private var bolEmail=false
    private var bolPassword=false
    lateinit var prefs:SharedPreferences
    private lateinit var mAuth:FirebaseAuth
    private lateinit var idTokenn:String

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
        prefs=context!!.getSharedPreferences(
            getString(R.string.parent_package_name),
            Context.MODE_PRIVATE
        )

        mAuth= FirebaseAuth.getInstance()

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
            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.fragment_main,
                SignUpFragment()
            )?.commit()
        }
    }

    //Action Button
    private fun btnActionSetOnClickListener() {
        pbLogin.visibility=View.INVISIBLE
        btnLAction.setOnClickListener {
            if (validation()) {
                actionWhileLoggingIn()

                val email = etLEmail.text.toString().trim()
                val password = etLPassword.text.toString().trim()

                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            getIDToken()
                        } else {
                            toastMaker("Failed To Login - " + task.exception?.message)
                            actionWhenLoginFailed()
                        }
                    }
            }
        }
    }

    private fun actionWhenLoginFailed() {
        pbLogin.visibility = View.INVISIBLE
        btnLAction.text = activity?.getString(R.string.login)
        btnLLogin.isEnabled=true
        btnLSignUp.isEnabled=true
        btnLAction.isEnabled=true
    }

    private fun actionWhileLoggingIn() {
        pbLogin.visibility=View.VISIBLE
        btnLAction.text=""
        btnLSignUp.isEnabled=false
        btnLLogin.isEnabled=false
        btnLAction.isEnabled=false
    }

    private fun getIDToken() {
        idTokenn=""
        mAuth.currentUser!!.getIdToken(true).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i(LOGINFRAGTAG,idTokenn)
                idTokenn = it.result!!.token!!
                if (idTokenn!="") {
                    callingAfterGettingIdToken()
                }
                else{
                    toastMaker("SERVER ERROR")
                    actionWhenLoginFailed()
                }
            }
            else{
                toastMaker("SERVER ERROR")
                actionWhenLoginFailed()
            }
        }
    }

    private fun callingAfterGettingIdToken() {
        RetrofitClient.instance.idToken=idTokenn
        RetrofitClient.instance.dashboardService.getUserRole()
            .enqueue(object : Callback<DashboardDefaultResponse> {
                override fun onResponse(
                    call: Call<DashboardDefaultResponse>,
                    response: Response<DashboardDefaultResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.i(LOGINFRAGTAG,response.body()!!.role.toString())
                        when (response.body()?.role) {
                            0 -> {
                                prefs.edit().putInt(activity?.getString(R.string.role), 1).apply()
                                Log.i(LOGINFRAGTAG,"DONOR")
                            }
                            1 -> {
                                prefs.edit().putInt(activity?.getString(R.string.role), 2).apply()
                                Log.i(LOGINFRAGTAG,"RECIPIENT")
                            }
                            2 -> {
                                prefs.edit().putInt(activity?.getString(R.string.role), 3).apply()
                                Log.i(LOGINFRAGTAG,"MODERATOR")
                            }
                        }
                        goingToDashboard()
                    } else {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Log.i(LOGINFRAGTAG, response.toString())
                        Log.i(LOGINFRAGTAG, jObjError.getString("message"))
                        toastMaker("Login failed"+jObjError.getString("message"))
                        actionWhenLoginFailed()
                    }
                }

                override fun onFailure(call: Call<DashboardDefaultResponse>, t: Throwable) {
                    Log.i(LOGINFRAGTAG, "error" + t.message)
                    toastMaker("No Internet / Server Down")
                    actionWhenLoginFailed()
                }
            })

    }

    private fun toastMaker(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun goingToDashboard() {
        toastMaker("Logging In")

        Log.i(LOGINFRAGTAG,prefs.getInt(activity?.getString(R.string.role),0).toString())

        prefs.edit().putBoolean(getString(R.string.is_Logged_In), true).apply()

        val intent = Intent(context, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }

    //validate the input
    private fun validation(): Boolean {
        return if(etLEmail.text.isNullOrEmpty() || etLEmail.text.isNullOrBlank()){
            toastMaker("Email cannot be Empty")
            Log.i(LOGINFRAGTAG, "Email cannot be Empty")
            false
        } else if(etLPassword.text.isNullOrEmpty() || etLPassword.text.isNullOrBlank()) {
            toastMaker("Password cannot be Empty")
            Log.i(LOGINFRAGTAG, "Password cannot be Empty")
            false
        } else{
            val temp=etLPassword.text.toString().trim()
            if(temp.length<6){
               toastMaker("Password should be greater than 5")
                Log.i(LOGINFRAGTAG, "Password should be greater than 5")
                false
            }
            else{
                true
            }
        }
    }

    private fun btnLoginSetOnClickListener() {}


}


package com.adoptvillage.bridge.fragment


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.DashboardActivity
import com.adoptvillage.bridge.activity.systemDarkGray
import com.adoptvillage.bridge.activity.systemViolet
import com.adoptvillage.bridge.models.Login
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_log_in.*



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
    lateinit var mAuth:FirebaseAuth

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
                pbLogin.visibility=View.VISIBLE
                btnLAction.text=""

                val email = etLEmail.text.toString().trim()
                val password = etLPassword.text.toString().trim()
                val obj = Login(email, password)



                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult?> {
                        override fun onComplete(task: Task<AuthResult?>) {
                            if (task.isSuccessful){
                                Snackbar.make(
                                    clMainScreen,
                                    "Logging In",
                                    Snackbar.LENGTH_INDEFINITE
                                ).show()
                                prefs.edit().putBoolean(getString(R.string.is_Logged_In), true).apply()
                                val intent = Intent(context, DashboardActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                                pbLogin.visibility = View.INVISIBLE
                                btnLAction.text = activity?.getString(R.string.login)
                            }
                            else{
                                Snackbar.make(
                                    clMainScreen,
                                    "Failed To Login - " + task.exception.toString(),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                pbLogin.visibility = View.INVISIBLE
                                btnLAction.text = activity?.getString(R.string.login)
                            }
                        }
                    })
            }
        }
    }

    //validate the input
    private fun validation(): Boolean {
        return if(etLEmail.text.isNullOrEmpty() || etLEmail.text.isNullOrBlank()){
            Snackbar.make(clMainScreen, "Email cannot be Empty", Snackbar.LENGTH_SHORT).show()
            Log.i(LOGINFRAGTAG, "Email cannot be Empty")
            false
        } else if(etLPassword.text.isNullOrEmpty() || etLPassword.text.isNullOrBlank()) {
            Snackbar.make(clMainScreen, "Password cannot be Empty", Snackbar.LENGTH_SHORT).show()
            Log.i(LOGINFRAGTAG, "Password cannot be Empty")
            false
        } else{
            val temp=etLPassword.text.toString().trim()
            if(temp.length<6){
                Snackbar.make(
                    clMainScreen,
                    "Password should be greater than 5",
                    Snackbar.LENGTH_SHORT
                ).show()
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


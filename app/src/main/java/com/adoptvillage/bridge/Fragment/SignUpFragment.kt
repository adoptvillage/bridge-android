package com.adoptvillage.bridge.Fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.Activity.systemDarkGray
import com.adoptvillage.bridge.Activity.systemGray
import com.adoptvillage.bridge.Activity.systemViolet
import com.adoptvillage.bridge.Models.Register
import com.adoptvillage.bridge.Models.RegisterDefaultResponse
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.Service.RetrofitClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.clMainScreen
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private var role:Int=-1
private var SIGNUPFRAGTAG="SIGNUPFRAGTAG"

class SignUpFragment : Fragment() {

    var bolName=false
    var bolEmail=false
    var bolPassword=false
    var bolConfirmPassword=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.explode)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        role=-1

        btnLoginSetOnClickListener()
        btnActionSetOnClickListener()
        btnDonorSetOnClickListener()
        btnRecipientSetOnClickListener()
        btnSignUpSetOnClickListener()
        btnUniversitySetOnClickListener()
        btnSActionEnableListener()
    }

    private fun btnSActionEnableListener() {
        btnSAction.isEnabled=false
        btnSAction.isActivated=false
        btnSAction.setBackgroundColor(Color.parseColor(systemDarkGray))
        btnSAction.setTextColor(Color.parseColor(systemViolet))

        etSName.addTextChangedListener {
            bolName = !(it.isNullOrBlank() || it.isNullOrEmpty())
            btnSAction.isEnabled = bolName && bolEmail && bolPassword && bolConfirmPassword && role!=-1
            btnSAction.isActivated = bolName && bolEmail && bolPassword && bolConfirmPassword && role!=-1
            if (bolName && bolEmail && bolPassword && bolConfirmPassword && role!=-1){
                btnSAction.setBackgroundColor(Color.parseColor(systemViolet))
                btnSAction.setTextColor(Color.WHITE)
            }
            else{
                btnSAction.setBackgroundColor(Color.parseColor(systemDarkGray))
                btnSAction.setTextColor(Color.parseColor(systemViolet))
            }
        }
        etSEmail.addTextChangedListener {
            bolEmail = !(it.isNullOrBlank() || it.isNullOrEmpty())
            btnSAction.isEnabled = bolName && bolEmail && bolPassword && bolConfirmPassword && role!=-1
            btnSAction.isActivated = bolName && bolEmail && bolPassword && bolConfirmPassword && role!=-1
            if (bolName && bolEmail && bolPassword && bolConfirmPassword && role!=-1){
                btnSAction.setBackgroundColor(Color.parseColor(systemViolet))
                btnSAction.setTextColor(Color.WHITE)
            }
            else{
                btnSAction.setBackgroundColor(Color.parseColor(systemDarkGray))
                btnSAction.setTextColor(Color.parseColor(systemViolet))
            }
        }
        etSPassword.addTextChangedListener {
            bolPassword = !(it.isNullOrEmpty() || it.isNullOrBlank() || it?.length<6)
            btnSAction.isEnabled = bolName && bolEmail && bolPassword && bolConfirmPassword && role!=-1
            btnSAction.isActivated = bolName && bolEmail && bolPassword && bolConfirmPassword && role!=-1
            if (bolName && bolEmail && bolPassword && bolConfirmPassword && role!=-1){
                btnSAction.setBackgroundColor(Color.parseColor(systemViolet))
                btnSAction.setTextColor(Color.WHITE)
            }
            else{
                btnSAction.setBackgroundColor(Color.parseColor(systemDarkGray))
                btnSAction.setTextColor(Color.parseColor(systemViolet))
            }
        }
        etSConfirmPassword.addTextChangedListener {
            bolConfirmPassword = !(it.isNullOrEmpty() || it.isNullOrBlank() || it?.length<6)
            btnSAction.isEnabled = bolName && bolEmail && bolPassword && bolConfirmPassword && role!=-1
            btnSAction.isActivated = bolName && bolEmail && bolPassword && bolConfirmPassword && role!=-1
            if (bolName && bolEmail && bolPassword && bolConfirmPassword && role!=-1){
                btnSAction.setBackgroundColor(Color.parseColor(systemViolet))
                btnSAction.setTextColor(Color.WHITE)
            }
            else{
                btnSAction.setBackgroundColor(Color.parseColor(systemDarkGray))
                btnSAction.setTextColor(Color.parseColor(systemViolet))
            }
        }
    }

    private fun btnUniversitySetOnClickListener() {
        btnSUniversity.setOnClickListener {
            btnSUniversity.setBackgroundColor(Color.parseColor(systemViolet))
            btnSUniversity.setTextColor(Color.WHITE)
            btnSRecipient.setBackgroundColor(Color.parseColor(systemGray))
            btnSRecipient.setTextColor(Color.parseColor(systemViolet))
            btnSDonor.setBackgroundColor(Color.parseColor(systemGray))
            btnSDonor.setTextColor(Color.parseColor(systemViolet))
            role=2
            if (bolName && bolEmail && bolPassword && bolConfirmPassword && role!=-1){
                btnSAction.setBackgroundColor(Color.parseColor(systemViolet))
                btnSAction.setTextColor(Color.WHITE)
                btnSAction.isEnabled=true
                btnSAction.isActivated=true
            }
            else{
                btnSAction.setBackgroundColor(Color.parseColor(systemDarkGray))
                btnSAction.setTextColor(Color.parseColor(systemViolet))
                btnSAction.isEnabled=false
                btnSAction.isActivated=false
            }
        }
    }

    private fun btnSignUpSetOnClickListener() {  }

    private fun btnRecipientSetOnClickListener() {
        btnSRecipient.setOnClickListener {
            btnSRecipient.setBackgroundColor(Color.parseColor(systemViolet))
            btnSRecipient.setTextColor(Color.WHITE)
            btnSDonor.setBackgroundColor(Color.parseColor(systemGray))
            btnSDonor.setTextColor(Color.parseColor(systemViolet))
            btnSUniversity.setBackgroundColor(Color.parseColor(systemGray))
            btnSUniversity.setTextColor(Color.parseColor(systemViolet))
            role=1
            if (bolName && bolEmail && bolPassword && bolConfirmPassword && role!=-1){
                btnSAction.setBackgroundColor(Color.parseColor(systemViolet))
                btnSAction.setTextColor(Color.WHITE)
                btnSAction.isEnabled=true
                btnSAction.isActivated=true
            }
            else{
                btnSAction.setBackgroundColor(Color.parseColor(systemDarkGray))
                btnSAction.setTextColor(Color.parseColor(systemViolet))
                btnSAction.isEnabled=false
                btnSAction.isActivated=false
            }
        }
    }

    private fun btnDonorSetOnClickListener() {
        btnSDonor.setOnClickListener {
            btnSDonor.setBackgroundColor(Color.parseColor(systemViolet))
            btnSDonor.setTextColor(Color.WHITE)
            btnSRecipient.setBackgroundColor(Color.parseColor(systemGray))
            btnSRecipient.setTextColor(Color.parseColor(systemViolet))
            btnSUniversity.setBackgroundColor(Color.parseColor(systemGray))
            btnSUniversity.setTextColor(Color.parseColor(systemViolet))
            role=0
            if (bolName && bolEmail && bolPassword && bolConfirmPassword && role!=-1){
                btnSAction.setBackgroundColor(Color.parseColor(systemViolet))
                btnSAction.setTextColor(Color.WHITE)
                btnSAction.isEnabled=true
                btnSAction.isActivated=true
            }
            else{
                btnSAction.setBackgroundColor(Color.parseColor(systemDarkGray))
                btnSAction.setTextColor(Color.parseColor(systemViolet))
                btnSAction.isEnabled=false
                btnSAction.isActivated=false
            }
        }
    }

    private fun btnActionSetOnClickListener() {
        pbSignUp.visibility=View.INVISIBLE
        btnSAction.setOnClickListener {
            if (validation()) {
                pbSignUp.visibility=View.VISIBLE
                btnSAction.text=""

                val name = etSName.text.toString().trim()
                val email = etSEmail.text.toString().trim()
                val password = etSPassword.text.toString().trim()
                val obj = Register(name, role, email, password)

                RetrofitClient.instance.authService.registerUser(obj).enqueue(object :
                    Callback<RegisterDefaultResponse> {
                    override fun onResponse(
                        call: Call<RegisterDefaultResponse>,
                        response: Response<RegisterDefaultResponse>
                    ) {
                        if (response.isSuccessful) {
                            Log.i(SIGNUPFRAGTAG, response.toString())
                            Log.i(SIGNUPFRAGTAG, response.body()?.message)
                            Snackbar.make(clMainScreen,"Registered!", Snackbar.LENGTH_INDEFINITE).show()
                            pbSignUp.visibility=View.INVISIBLE
                            btnSAction.text=getString(R.string.signup)
                        } else {
                            Log.i(SIGNUPFRAGTAG, response.toString())
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            Log.i(SIGNUPFRAGTAG, jObjError.getString("message"))
                            Snackbar.make(clMainScreen,jObjError.getString("message"),Snackbar.LENGTH_LONG).show()
                            pbSignUp.visibility=View.INVISIBLE
                            btnSAction.text=getString(R.string.signup)
                        }
                    }

                    override fun onFailure(call: Call<RegisterDefaultResponse>, t: Throwable) {
                        Log.i(SIGNUPFRAGTAG, t.message)
                        Snackbar.make(clMainScreen,"Failed To Login - "+t.message,Snackbar.LENGTH_LONG).show()
                        pbSignUp.visibility=View.INVISIBLE
                        btnSAction.text=getString(R.string.signup)
                    }
                })
            }
        }
    }

    private fun btnLoginSetOnClickListener() {
        btnSLogin.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_main, LogInFragment())?.commit()
        }
    }

    private fun validation(): Boolean {
        return if(etSName.text.isNullOrEmpty() || etSName.text.isNullOrBlank()) {
            Snackbar.make(clMainScreen,"Name cannot be Empty", Snackbar.LENGTH_SHORT).show()
            Log.i(SIGNUPFRAGTAG,"Name cannot be Empty")
            false
        } else if(etSPassword.text.isNullOrEmpty() || etSPassword.text.isNullOrBlank()) {
            Snackbar.make(clMainScreen,"Password cannot be Empty", Snackbar.LENGTH_SHORT).show()
            Log.i(SIGNUPFRAGTAG,"Password cannot be Empty")
            false
        } else if(etSEmail.text.isNullOrEmpty() || etSEmail.text.isNullOrBlank()){
            Snackbar.make(clMainScreen,"Email cannot be Empty", Snackbar.LENGTH_SHORT).show()
            Log.i(SIGNUPFRAGTAG,"Email cannot be Empty")
            false
        } else if (etSPassword.text.toString()!=etSConfirmPassword.text.toString()){
            Snackbar.make(clMainScreen,"Confirm Password Invalid", Snackbar.LENGTH_SHORT).show()
            Log.i(SIGNUPFRAGTAG,"Confirm Password Invalid")
            false
        }
        else{
            val temp=etSPassword.text.toString().trim()
            if(temp.length<6){
                Snackbar.make(clMainScreen,"Password should be greater than 5", Snackbar.LENGTH_SHORT).show()
                Log.i(SIGNUPFRAGTAG,"Password should be greater than 5")
                false
            }
            else{
                true
            }
        }
    }
}


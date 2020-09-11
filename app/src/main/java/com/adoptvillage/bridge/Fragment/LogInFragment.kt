package com.adoptvillage.bridge.Fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.adoptvillage.bridge.Activity.systemGray
import com.adoptvillage.bridge.Activity.systemViolet
import com.adoptvillage.bridge.Models.Login
import com.adoptvillage.bridge.Models.LoginDefaultResponse
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.Service.RetrofitClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_log_in.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private var role:Int=0
private var LOGINFRAGTAG="LOGINFRAGTAG"

class LogInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLoginSetOnClickListener()
        btnActionSetOnClickListener()
        btnDonorSetOnClickListener()
        btnRecipientSetOnClickListener()
        btnSignUpSetOnClickListener()
        btnUniversitySetOnClickListener()
    }
    private fun btnUniversitySetOnClickListener() {
        btnLUniversity.setOnClickListener {
            btnLUniversity.setBackgroundColor(Color.parseColor(systemViolet))
            btnLUniversity.setTextColor(Color.WHITE)
            btnLRecipient.setBackgroundColor(Color.parseColor(systemGray))
            btnLRecipient.setTextColor(Color.parseColor(systemViolet))
            btnLDonor.setBackgroundColor(Color.parseColor(systemGray))
            btnLDonor.setTextColor(Color.parseColor(systemViolet))
            role=2
        }
    }

    private fun btnSignUpSetOnClickListener() {
        btnLSignUp.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_main, SignUpFragment())?.commit()
        }
    }

    private fun btnRecipientSetOnClickListener() {
        btnLRecipient.setOnClickListener {
            btnLRecipient.setBackgroundColor(Color.parseColor(systemViolet))
            btnLRecipient.setTextColor(Color.WHITE)
            btnLDonor.setBackgroundColor(Color.parseColor(systemGray))
            btnLDonor.setTextColor(Color.parseColor(systemViolet))
            btnLUniversity.setBackgroundColor(Color.parseColor(systemGray))
            btnLUniversity.setTextColor(Color.parseColor(systemViolet))
            role=1
        }
    }

    private fun btnDonorSetOnClickListener() {
        btnLDonor.setOnClickListener {
            btnLDonor.setBackgroundColor(Color.parseColor(systemViolet))
            btnLDonor.setTextColor(Color.WHITE)
            btnLRecipient.setBackgroundColor(Color.parseColor(systemGray))
            btnLRecipient.setTextColor(Color.parseColor(systemViolet))
            btnLUniversity.setBackgroundColor(Color.parseColor(systemGray))
            btnLUniversity.setTextColor(Color.parseColor(systemViolet))
            role=0
        }
    }

    private fun btnActionSetOnClickListener() {
        btnLAction.setOnClickListener {
            if (validation()) {

                val email = etLEmail.text.toString().trim()
                val password = etLPassword.text.toString().trim()
                val obj = Login(email, password)

                RetrofitClient.instance.loginUser(obj)
                    .enqueue(object : Callback<LoginDefaultResponse> {
                        override fun onResponse(
                            call: Call<LoginDefaultResponse>,
                            response: Response<LoginDefaultResponse>
                        ) {
                            if (response.isSuccessful) {
                                Log.i(LOGINFRAGTAG, response.toString())
                                Log.i(LOGINFRAGTAG, response.body()?.message)
                                Toast.makeText(
                                    context,
                                    response.body()?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val jObjError = JSONObject(response.errorBody()!!.string())
                                Log.i(LOGINFRAGTAG, response.toString())
                                Log.i(LOGINFRAGTAG, jObjError.getString("message"))
                                Toast.makeText(
                                    context,
                                    jObjError.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<LoginDefaultResponse>, t: Throwable) {
                            Log.i(LOGINFRAGTAG, t.message)
                        }
                    })
            }
        }
    }

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
            if(temp.length<=6){
                Snackbar.make(clMainScreen,"Password should be greater than 6",Snackbar.LENGTH_SHORT).show()
                Log.i(LOGINFRAGTAG,"Password should be greater than 6")
                false
            }
            else{
                true
            }
        }
    }

    private fun btnLoginSetOnClickListener() {}
}

